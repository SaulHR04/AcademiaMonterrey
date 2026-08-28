package com.luv2code.springboot.cruddemo.rest;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * La taquilla: cambia usuario y contrasena por un token.
 *
 * Este es el UNICO sitio de la aplicacion donde todavia viaja la contrasena.
 * A partir de aqui, el cliente solo manda el token.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /** Lo que devuelve el login. Un record: clase inmutable en una linea. */
    public record TokenResponse(String accessToken, String tokenType, long expiresIn, String user) {
    }

    private final JwtEncoder jwtEncoder;

    private final long ttlSeconds;

    public AuthController(JwtEncoder theJwtEncoder,
            @Value("${jwt.ttl-seconds}") long theTtlSeconds) {
        jwtEncoder = theJwtEncoder;
        ttlSeconds = theTtlSeconds;
    }

    /**
     * POST /api/auth/login
     *
     * No recibe un @RequestBody con usuario y contrasena: para cuando este metodo se
     * ejecuta, la cadena de filtros 1 YA valido el HTTP Basic contra la base de datos.
     * Spring nos inyecta el resultado en el parametro Authentication.
     * Si las credenciales fueran malas, nunca llegariamos hasta aqui (401).
     */
    @PostMapping("/login")
    public TokenResponse login(Authentication authentication) {

        Instant ahora = Instant.now();

        // Los roles que salieron de la tabla "roles": ROLE_EMPLOYEE, ROLE_MANAGER...
        //
        // El filtro startsWith("ROLE_") NO es decorativo. Spring Security 7 agrega por su
        // cuenta autoridades que describen COMO te autenticaste (FACTOR_PASSWORD, y otras
        // FACTOR_* si usas multifactor). Son utiles dentro del servidor, pero no tienen
        // nada que hacer dentro de un token que viaja al cliente. Sin este filtro, el
        // token saldria con "roles":["ROLE_EMPLOYEE","FACTOR_PASSWORD"].
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .collect(Collectors.toList());

        // El PAYLOAD del token. Todo esto viaja en claro dentro del token:
        // va FIRMADO, no cifrado. Nunca pongas aqui datos secretos.
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("security-jwt")                        // quien lo emitio
                .issuedAt(ahora)                               // cuando  (claim "iat")
                .expiresAt(ahora.plusSeconds(ttlSeconds))      // hasta cuando (claim "exp")
                .subject(authentication.getName())             // de quien es (claim "sub")
                .claim("roles", roles)                         // que puede hacer
                .build();

        // firmar con RS256 = RSA + SHA-256, usando la llave privada
        JwsHeader header = JwsHeader.with(org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.RS256).build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new TokenResponse(token, "Bearer", ttlSeconds, authentication.getName());
    }
}

package com.luv2code.springboot.cruddemo.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad con OAuth2. Compara este archivo con el del proyecto 02
 * y fijate en todo lo que DESAPARECIO:
 *
 *   - no hay UserDetailsService  -> los usuarios ya no viven aqui, viven en Keycloak
 *   - no hay JwtEncoder          -> esta API ya no emite tokens
 *   - no hay JwtDecoder          -> la llave publica se descarga sola del emisor
 *   - no hay llaves RSA          -> no hay nada que guardar ni que rotar
 *   - no hay AuthController      -> el login pasa en Keycloak, no aqui
 *
 * Esta API se volvio un RESOURCE SERVER puro: solo sabe validar tokens ajenos.
 */
@Configuration
public class SecurityConfig {

    /**
     * Las reglas de autorizacion: IDENTICAS a las de los proyectos 01 y 02.
     * Tres formas distintas de autenticar, exactamente el mismo control de acceso.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET,    "/api/employees").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET,    "/api/employees/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.POST,   "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT,    "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PATCH,  "/api/employees/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        // Toda la validacion de tokens cabe aqui. La llave publica la descarga Spring
        // solo, desde el issuer-uri de application.properties.
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.jwtAuthenticationConverter(keycloakConverter())));

        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Keycloak no pone los roles donde Spring los busca por defecto.
     *
     * Spring espera un claim plano (por defecto "scope"). Keycloak los mete ANIDADOS,
     * dentro de "realm_access", y sin el prefijo ROLE_:
     *
     *   "realm_access": { "roles": ["EMPLOYEE", "MANAGER", "default-roles-academy"] }
     *
     * Asi que hay que bajar dos niveles y agregar el prefijo nosotros. Este metodo es
     * la unica "traduccion" que necesita la aplicacion, y es la razon por la que las
     * reglas de arriba pudieron quedarse idenricas a las del proyecto 01.
     */
    private Converter<Jwt, AbstractAuthenticationToken> keycloakConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(SecurityConfig::extraerRoles);

        return converter;
    }

    @SuppressWarnings("unchecked")
    private static Collection<GrantedAuthority> extraerRoles(Jwt jwt) {

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null || realmAccess.get("roles") == null) {
            return List.of();
        }

        List<String> roles = (List<String>) realmAccess.get("roles");

        return roles.stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
}

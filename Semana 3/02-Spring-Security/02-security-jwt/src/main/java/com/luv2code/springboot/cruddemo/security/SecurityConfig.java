package com.luv2code.springboot.cruddemo.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * Configuracion de seguridad con JWT.
 *
 * La gran diferencia con el proyecto 01: aqui hay DOS cadenas de filtros,
 * porque hay dos formas distintas de entrar a la aplicacion.
 *
 *   /api/auth/login  -> con usuario y contrasena (HTTP Basic). Se usa UNA vez.
 *   /api/employees   -> con el token que devolvio el login (Bearer).
 */
@Configuration
public class SecurityConfig {

    // Spring convierte solo los archivos .pem en objetos de llave RSA
    @Value("${rsa.public-key}")
    private RSAPublicKey publicKey;

    @Value("${rsa.private-key}")
    private RSAPrivateKey privateKey;

    /**
     * De donde salen los usuarios. IDENTICO al proyecto 01: las mismas tablas.
     * El login sigue necesitando comprobar usuario y contrasena contra la base de datos;
     * lo que cambia es que eso ya solo pasa UNA vez, no en cada peticion.
     */
    @Bean
    public UserDetailsService userDetailsService(DataSource theDataSource) {

        JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(theDataSource);

        theUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, pw, active from members where user_id=?");

        theUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?");

        return theUserDetailsManager;
    }

    /**
     * CADENA 1 - solo para /api/auth/**  (la taquilla donde compras el boleto).
     *
     * securityMatcher dice "esta cadena solo aplica a estas rutas". Como tiene
     * @Order(1), Spring la revisa primero; si la ruta no encaja, prueba la siguiente.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher("/api/auth/**");
        http.authorizeHttpRequests(configurer -> configurer.anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * CADENA 2 - todo lo demas (el torniquete donde se valida el boleto).
     *
     * Fijate en las reglas de autorizacion: son EXACTAMENTE las mismas del proyecto 01.
     * No cambia quien puede hacer que; cambia de donde salen los roles.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET,    "/api/employees").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET,    "/api/employees/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.POST,   "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT,    "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PATCH,  "/api/employees/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        // Aqui esta el cambio de fondo: ya no se acepta HTTP Basic.
        // Esta cadena solo entiende "Authorization: Bearer <token>".
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * FIRMAR tokens. Usa la llave PRIVADA: solo este servidor puede emitir tokens validos.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    /**
     * VALIDAR tokens. Usa la llave PUBLICA. Cualquiera podria validar; nadie mas puede firmar.
     * Esa asimetria es todo el truco de la criptografia de llave publica.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    /**
     * Traduce los claims del token a roles de Spring.
     *
     * Por defecto Spring lee el claim "scope" y le pega el prefijo "SCOPE_", con lo que
     * hasRole("EMPLOYEE") dejaria de funcionar. Como nuestro token trae un claim "roles"
     * que YA dice "ROLE_EMPLOYEE", le decimos: lee "roles" y no le agregues nada.
     * Gracias a esto las reglas de la cadena 2 son identicas a las del proyecto 01.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}

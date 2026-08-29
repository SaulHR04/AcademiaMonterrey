package com.luv2code.springboot.cruddemo.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad con HTTP Basic.
 *
 * Dos beans, dos responsabilidades distintas:
 *
 *   userDetailsService -> DE DONDE salen los usuarios  (autenticacion: quien eres)
 *   filterChain        -> QUIEN puede hacer QUE        (autorizacion: que puedes hacer)
 */
@Configuration
public class SecurityConfig {

    /**
     * Le decimos a Spring que los usuarios viven en la base de datos.
     *
     * Spring trae un esquema por defecto (tablas "users" y "authorities"). El nuestro
     * se llama distinto (members / roles), asi que hay que decirle como consultarlo.
     * Spring NO adivina tu esquema.
     *
     * Las dos consultas reciben el username como unico parametro (el "?").
     */
    @Bean
    public UserDetailsService userDetailsService(DataSource theDataSource) {

        JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(theDataSource);

        // como buscar un usuario: debe devolver username, password y si esta activo
        theUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, pw, active from members where user_id=?");

        // como buscar sus roles: debe devolver username y rol
        theUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?");

        return theUserDetailsManager;
    }

    /**
     * La cadena de filtros: TODO request pasa por aqui antes de llegar al @RestController.
     *
     * OJO con hasRole("EMPLOYEE"): en la base de datos el rol se guarda como
     * "ROLE_EMPLOYEE", pero aqui se escribe SIN el prefijo. Spring lo agrega solo.
     * Si escribes hasRole("ROLE_EMPLOYEE") buscara "ROLE_ROLE_EMPLOYEE" y nada funcionara.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> configurer
                // leer: cualquier empleado
                .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")
                // crear y modificar: solo managers
                .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PATCH, "/api/employees/**").hasRole("MANAGER")
                // borrar: solo admins
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
                // cualquier otra cosa: al menos hay que estar autenticado
                .anyRequest().authenticated());

        // usar autenticacion HTTP Basic
        http.httpBasic(Customizer.withDefaults());

        // Desactivamos CSRF porque esta es una API REST sin sesiones ni cookies.
        // CSRF protege contra que OTRA pagina use la cookie de sesion del navegador;
        // si no hay cookie de sesion, no hay nada que robar. En una app web con
        // formularios y login por sesion, CSRF se deja ENCENDIDO.
        http.csrf(csrf -> csrf.disable());

        // Sin sesion en el servidor: cada request llega con sus credenciales
        // y se autentica desde cero. Eso es ser "stateless".
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

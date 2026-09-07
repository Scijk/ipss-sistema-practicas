package com.ipss.practicas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/practicas", "/api/practicas/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/practicas", "/api/practicas/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/practicas", "/api/practicas/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/practicas", "/api/practicas/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/practicas", "/api/practicas/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/usuarios", "/api/usuarios/**").hasAnyRole("PROFESOR", "ESTUDIANTE")
                .requestMatchers(HttpMethod.POST, "/api/usuarios", "/api/usuarios/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios", "/api/usuarios/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PATCH, "/api/usuarios", "/api/usuarios/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios", "/api/usuarios/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.GET, "/api/estudiantes", "/api/estudiantes/**").hasAnyAuthority("ROLE_PROFESOR", "ROLE_ESTUDIANTE")
                .requestMatchers(HttpMethod.POST, "/api/estudiantes", "/api/estudiantes/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PUT, "/api/estudiantes", "/api/estudiantes/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PATCH, "/api/estudiantes", "/api/estudiantes/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.DELETE, "/api/estudiantes", "/api/estudiantes/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.GET, "/api/profesores", "/api/profesores/**").hasAnyAuthority("ROLE_PROFESOR", "ROLE_ESTUDIANTE")
                .requestMatchers(HttpMethod.POST, "/api/profesores", "/api/profesores/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PUT, "/api/profesores", "/api/profesores/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PATCH, "/api/profesores", "/api/profesores/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.DELETE, "/api/profesores", "/api/profesores/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.GET, "/api/empresas", "/api/empresas/**").hasAnyAuthority("ROLE_PROFESOR", "ROLE_ESTUDIANTE")
                .requestMatchers(HttpMethod.POST, "/api/empresas", "/api/empresas/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PUT, "/api/empresas", "/api/empresas/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PATCH, "/api/empresas", "/api/empresas/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.DELETE, "/api/empresas", "/api/empresas/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.GET, "/api/jefes-directos", "/api/jefes-directos/**").hasAnyAuthority("ROLE_PROFESOR", "ROLE_ESTUDIANTE")
                .requestMatchers(HttpMethod.POST, "/api/jefes-directos", "/api/jefes-directos/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PUT, "/api/jefes-directos", "/api/jefes-directos/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.PATCH, "/api/jefes-directos", "/api/jefes-directos/**").hasAuthority("ROLE_PROFESOR")
                .requestMatchers(HttpMethod.DELETE, "/api/jefes-directos", "/api/jefes-directos/**").hasAuthority("ROLE_PROFESOR")
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

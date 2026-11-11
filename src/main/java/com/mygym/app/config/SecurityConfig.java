package com.mygym.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.mygym.app.security.JwtRoleConverter;
import com.mygym.app.security.PlatformEndpoints;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PlatformEndpoints.PUBLIC).permitAll()
                        .requestMatchers("/api/admins/login", "/api/admins/primaryexists", "/api/admins/createprimary").permitAll()
                        .requestMatchers("/api/trainers/login", "/api/members/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/admins", "/api/trainers", "/api/members").permitAll()
                        .requestMatchers("/api/admins/**", "/api/trainers/**", "/api/members/**")
                            .hasAnyRole("ADMIN", "TRAINER", "MEMBER")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(JwtRoleConverter.create())))
                .build();
    }
}

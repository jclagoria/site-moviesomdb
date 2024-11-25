package com.omdb.api.config;

import com.omdb.api.config.security.JwtReactiveAuthenticationManager;
import com.omdb.api.config.security.JwtTokenFilter;
import com.omdb.api.config.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/auth/**").permitAll() // Permitir rutas públicas
                        .anyExchange().authenticated())          // Todas las demás requieren autenticación
                .authenticationManager(authenticationManager())  // Configura autenticación
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return authentication -> {
            String token = authentication.getCredentials().toString();
            String username = jwtTokenProvider.getUsername(token);

            if (username == null) {
                return Mono.error(new RuntimeException("Token inválido"));
            }

            return Mono.just(new UsernamePasswordAuthenticationToken(username, null, null));
        };
    }

}

package com.omdb.api.config;

import com.omdb.api.config.security.JwtReactiveAuthenticationManager;
import com.omdb.api.config.security.JwtSecurityContextRepository;
import com.omdb.api.config.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

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
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/***").permitAll()
                )
                .authenticationManager(reactiveAuthenticationManager())
                .securityContextRepository(securityContextRepository())
                .build();
    }

    @Bean
    @Primary
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return new JwtReactiveAuthenticationManager(jwtTokenProvider);
    }

    @Bean
    public ServerSecurityContextRepository securityContextRepository() {
        return new JwtSecurityContextRepository(jwtTokenProvider, reactiveAuthenticationManager());
    }

    @Bean(name = "jwtAuthenticationManager")
    public ReactiveAuthenticationManager jwtReactiveAuthenticationManager() {
        return new JwtReactiveAuthenticationManager(jwtTokenProvider);
    }

}

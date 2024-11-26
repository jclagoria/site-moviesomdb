package com.omdb.api.config.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtReactiveAuthenticationManager(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        if (!jwtTokenProvider.validateToken(token)) {
            return Mono.error(new BadCredentialsException("Token inválido"));
        }

        String username = jwtTokenProvider.getUsername(token);
        // Aquí puedes agregar roles y permisos si se manejan en tu aplicación
        return Mono.just(new UsernamePasswordAuthenticationToken(username, token, Collections.emptyList()));
    }
}

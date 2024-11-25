package com.omdb.api.auth.service;

import com.omdb.api.auth.dto.RegisterRequest;
import com.omdb.api.auth.model.UserApp;
import com.omdb.api.repository.UserAppRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UserAppRepository userAppRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserAppRepository userAppRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userAppRepository = userAppRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Mono<Object> registerUser(RegisterRequest registerRequest) {
        return userAppRepository.findByUsername(registerRequest.username())
                .flatMap(existingUser -> Mono.error(new RuntimeException("Username already exists")))
                .switchIfEmpty(userAppRepository.findByEmail(registerRequest.email())
                        .flatMap(existingEmail -> Mono.error(new RuntimeException("Email already exists")))
                )
                .switchIfEmpty(Mono.defer(() -> {
                    String encodedPassword = bCryptPasswordEncoder.encode(registerRequest.password());
                    UserApp user = new UserApp(registerRequest.username(),
                            encodedPassword,
                            registerRequest.email());
                    return userAppRepository.save(user);
                }));
    }

}

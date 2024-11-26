package com.omdb.api.auth.service;

import com.omdb.api.auth.dto.LoginRequest;
import com.omdb.api.auth.dto.LoginResponse;
import com.omdb.api.auth.dto.RegisterRequest;
import com.omdb.api.auth.exception.InvalidCredentialsException;
import com.omdb.api.auth.exception.TokenCreationException;
import com.omdb.api.auth.exception.UserNotFoundException;
import com.omdb.api.auth.model.Token;
import com.omdb.api.auth.model.UserApp;
import com.omdb.api.config.security.JwtTokenProvider;
import com.omdb.api.repository.TokenRepository;
import com.omdb.api.repository.UserAppRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AuthService {

    private static final Logger authLogger = LoggerFactory.getLogger(AuthService.class);

    private final UserAppRepository userAppRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserAppRepository userAppRepository,
                       TokenRepository tokenRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userAppRepository = userAppRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Mono<Object> registerUser(RegisterRequest registerRequest) {
        authLogger.info("In registerUser method");
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

    public Mono<LoginResponse> authenticate(LoginRequest loginRequest) {
        authLogger.info("In authenticate method");
        return userAppRepository.findByUsername(loginRequest.username())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User does not exist")))
                .flatMap(user -> {
                    authLogger.info("User found, to validate the password");
                    if (bCryptPasswordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
                        return Mono.fromCallable(() -> {
                                    String token = jwtTokenProvider
                                            .createToken(user.getUsername(), user.getEmail());
                                    return new Token(user.getId(), token, true);
                                })
                                .flatMap(tokenRepository::save) // Save token to DB.
                                .map(savedToken -> new LoginResponse(user.getUsername(),
                                        savedToken.getToken()))
                                .onErrorResume(e -> Mono.error(new TokenCreationException("Failed to create token")));
                    } else {
                        return Mono.error(new InvalidCredentialsException("Invalid credentials"));
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

}

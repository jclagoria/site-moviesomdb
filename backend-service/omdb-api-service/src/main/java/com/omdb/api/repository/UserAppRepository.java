package com.omdb.api.repository;

import com.omdb.api.auth.model.UserApp;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserAppRepository extends ReactiveCrudRepository<UserApp, Long> {
    Mono<UserApp> findByUsername(String name);
    Mono<UserApp> findByEmail(String email);
}

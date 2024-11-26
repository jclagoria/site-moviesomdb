package com.omdb.api.repository;

import com.omdb.api.auth.model.Token;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TokenRepository extends ReactiveCrudRepository<Token, Long> {

}

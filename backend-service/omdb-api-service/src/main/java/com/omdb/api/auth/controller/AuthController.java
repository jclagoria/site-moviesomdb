package com.omdb.api.auth.controller;

import com.omdb.api.auth.dto.RegisterRequest;
import com.omdb.api.auth.service.AuthService;
import com.omdb.api.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<ApiResponse>> register(@RequestBody RegisterRequest request) {
        return authService.registerUser(request)
                .thenReturn(ResponseEntity.ok().body(new ApiResponse("El usuario se registro exitosamente",
                        HttpStatus.OK.value())))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest()
                        .body(new ApiResponse(ex.getMessage(),
                                HttpStatus.BAD_REQUEST.value()))));
    }

}

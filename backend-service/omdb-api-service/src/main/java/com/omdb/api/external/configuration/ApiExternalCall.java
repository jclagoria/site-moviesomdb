package com.omdb.api.external.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ApiExternalCall {

    private final WebClient webClient;

    public ApiExternalCall(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> getMonoObject(String uriEndPoint, Class<T> responseType) {
        return webClient.get()
                .uri(uriEndPoint)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Flux<T> getFluxObject(String uriEndPoint, Class<T> responseType) {
        return webClient.get()
                .uri(uriEndPoint)
                .retrieve()
                .bodyToFlux(responseType);
    }

}

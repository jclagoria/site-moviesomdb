package com.omdb.api.external.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ApiExternalCall {

    private static final Logger logger = LoggerFactory.getLogger(ApiExternalCall.class);
    private final WebClient webClient;

    public ApiExternalCall(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Fetches a single object from the API.
     *
     * @param uriEndPoint the endpoint URI.
     * @param responseType the expected response type.
     * @param <T> the type of the response.
     * @return a Mono containing the response object.
     */
    public <T> Mono<T> getMonoObject(String uriEndPoint, Class<T> responseType) {
        logger.info("In get MonoObject - Making GET request to {}", uriEndPoint);
        return webClient.get()
                .uri(uriEndPoint)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    logger.error("Recived error status: {}", response.statusCode());
                    return Mono.error(new WebClientResponseException(
                            "Error during Api Call",
                            response.statusCode().value(),
                            null, null, null, null
                    ));
                })
                .bodyToMono(responseType)
                .doOnError(e -> logger.error("Error during API call: {}", e.getMessage()));
    }

    /**
     * Fetches a collection of objects from the API.
     *
     * @param uriEndPoint the endpoint URI.
     * @param responseType the expected response type.
     * @param <T> the type of the response elements.
     * @return a Flux containing the response objects.
     */
    public <T> Flux<T> getFluxObject(String uriEndPoint, Class<T> responseType) {
        logger.info("In get FluxObject - Making GET request to {}", uriEndPoint);
        return webClient.get()
                .uri(uriEndPoint)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->{
                    logger.error("Recived error status: {}", response.statusCode());
                    return Mono.error(new WebClientResponseException(
                            "Error during API call",
                            response.statusCode().value(),
                            null,
                            null,
                            null,
                            null));
                })
                .bodyToFlux(responseType);
    }

}

package com.omdb.api.external.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Value("${external.api.url}")
    private String URL_OMDB_SERVICE;

    @Bean
    public WebClient getWebClient() {
        return WebClient
                .builder()
                .baseUrl(URL_OMDB_SERVICE)
                .build();
    }

}

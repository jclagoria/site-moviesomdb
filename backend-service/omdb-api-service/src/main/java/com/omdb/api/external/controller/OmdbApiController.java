package com.omdb.api.external.controller;

import com.omdb.api.external.dto.ResultMovieDto;
import com.omdb.api.external.service.OMDBApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/omdb")
public class OmdbApiController {

    private static final Logger logger = LoggerFactory.getLogger(OmdbApiController.class);

    private final OMDBApiService omdbApiService;

    public OmdbApiController(OMDBApiService omdbApiService) {
        this.omdbApiService = omdbApiService;
    }

    /**
     * Searches for movies based on criteria provided in the request.
     *
     * @param query the search query (required).
     * @param type the type of the content (e.g., movie, series, etc.) (optional).
     * @param year the release year (optional).
     * @param page the page number for pagination (optional, defaults to 1).
     * @param token the authorization token (required).
     * @return a Mono containing the search result.
     */
    @GetMapping("/search")
    public Mono<ResultMovieDto> searchByCriteria(
            @RequestParam String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestHeader("Authorization") String token)
    {
        logger.info("Received search request: query={}, type={}, year={}, page={}", query, type, year, page);

        return omdbApiService.searchByCriteria(query, type, year, page)
                .doOnError(error -> logger.error("Error during search: {}", error.getMessage(), error));
    }

}

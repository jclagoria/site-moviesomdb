package com.omdb.api.external.service;

import com.omdb.api.external.configuration.ApiExternalCall;
import com.omdb.api.external.dto.ResultMovieDto;
import com.omdb.api.external.mapper.QueryResultMapper;
import com.omdb.api.external.model.Movie;
import com.omdb.api.external.model.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class OMDBApiService {

    private static final Logger logger = LoggerFactory.getLogger(OMDBApiService.class);

    private final String API_KEY_OMDB;
    private final ApiExternalCall apiExternalCall;

    public OMDBApiService(@Value("${external.api.key}")String apiKey, ApiExternalCall apiExternalCall) {
        this.API_KEY_OMDB = apiKey;
        this.apiExternalCall = apiExternalCall;
    }

    /**
     * Searches for movies based on the given criteria.
     *
     * @param query the search query.
     * @param type the type of the content (e.g., movie, series, etc.).
     * @param year the year of release.
     * @param page the page number for pagination.
     * @return a Mono containing the result in DTO format.
     */
    public Mono<ResultMovieDto> searchByCriteria(String query, String type, Integer year, Integer page) {

        if (query == null || query.isEmpty() || page == null || page < 0) {
            return Mono.error(new IllegalArgumentException("Invalid paremeters for criteria"));
        }

        String url = UriComponentsBuilder.fromUriString("/")
                .queryParam("apikey", API_KEY_OMDB)
                .queryParam("s", query)
                .queryParam("page", page)
                .queryParamIfPresent("t", Optional.ofNullable(type))
                .queryParamIfPresent("y", Optional.ofNullable(year))
                .toUriString();

        logger.info("Constructed OMDB wuery URL: {}", url);

        return apiExternalCall.getMonoObject(url, QueryResult.class)
                .flatMap(QueryResultMapper::mapToDTO)
                .onErrorResume(exception -> {
                    logger.error("Error calling OMDB API: {}", exception.getMessage(), exception);
                    return Mono.just(new ResultMovieDto(List.of(), 0, 0));
                });
    }

    public Mono<Movie> searchByIdIMDB(String id) {
        return null;
    }

}

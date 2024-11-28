package com.omdb.api.external.service;

import com.omdb.api.external.configuration.ApiExternalCall;
import com.omdb.api.external.dto.MovieDto;
import com.omdb.api.external.dto.ResultMovieDto;
import com.omdb.api.external.model.Movie;
import com.omdb.api.external.model.QueryResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OMDBApiService {

    private final String API_KEY_OMDB;
    private final ApiExternalCall apiExternalCall;

    public OMDBApiService(@Value("${external.api.key}")String apiKey, ApiExternalCall apiExternalCall) {
        this.API_KEY_OMDB = apiKey;
        this.apiExternalCall = apiExternalCall;
    }

    public Mono<ResultMovieDto> searchByCriteria(String query, String type, Integer year, Integer page) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("?apikey=").append(API_KEY_OMDB);
        queryBuilder.append("&s=").append(query);
        queryBuilder.append("&page=").append(page.intValue());

        if( type != null) {
            queryBuilder.append("&t=").append(type);
        }

        return apiExternalCall.getMonoObject(queryBuilder.toString(), QueryResult.class)
                .flatMap(this::mapToDTO);
    }

    private Mono<ResultMovieDto> mapToDTO(QueryResult result) {

        if (result.getSearch() == null || result.getResponse().equalsIgnoreCase("False")) {
            return Mono.just(new ResultMovieDto(List.of(), 0, 0));
        }

        int totalResults = Integer.parseInt(result.getTotalResults());
        int totalPages = (int) Math.ceil(totalResults / 10.0);

        List<MovieDto> resultMovies = result.getSearch().stream()
                .map(movie ->
                        new MovieDto(
                            movie.getImdbID(),
                            movie.getTitle(),
                            movie.getYear(),
                            movie.getType(),
                            movie.getPoster()
                ))
                .toList();

        return Mono.just(new ResultMovieDto(resultMovies, totalResults, totalPages));
    }

    public Mono<Movie> searchByIdIMDB(String id) {
        return null;
    }

}

package com.omdb.api.external.mapper;

import com.omdb.api.external.dto.MovieDto;
import com.omdb.api.external.dto.ResultMovieDto;
import com.omdb.api.external.model.QueryResult;
import reactor.core.publisher.Mono;

import java.util.List;

public class QueryResultMapper {

    private QueryResultMapper() {
    }

    public static Mono<ResultMovieDto> mapToDTO(QueryResult queryResult) {

        if (queryResult.getSearch() == null
                || queryResult.getResponse().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return Mono.just(new ResultMovieDto(List.of(), 0, 0));
        }

        int totalResults = Integer.parseInt(queryResult.getTotalResults());
        int totalPages = (int) Math.ceil(totalResults / 10.0);

        List<MovieDto> resultMovies = queryResult.getSearch().stream()
                .map(movie -> new MovieDto(
                        movie.getImdbID(),
                        movie.getTitle(),
                        movie.getYear(),
                        movie.getType(),
                        movie.getPoster()
                ))
                .toList();

        return Mono.just(new ResultMovieDto(resultMovies, totalResults, totalPages));
    }

}

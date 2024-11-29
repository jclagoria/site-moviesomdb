package com.omdb.api.external.dto;

import java.util.List;

public record ResultMovieDto(List<MovieDto> movies, int totalResults, int totalPages) {
}

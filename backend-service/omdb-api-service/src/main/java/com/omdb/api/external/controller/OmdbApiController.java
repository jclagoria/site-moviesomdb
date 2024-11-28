package com.omdb.api.external.controller;

import com.omdb.api.external.dto.ResultMovieDto;
import com.omdb.api.external.service.OMDBApiService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/omdb")
public class OmdbApiController {

    private final OMDBApiService omdbApiService;

    public OmdbApiController(OMDBApiService omdbApiService) {
        this.omdbApiService = omdbApiService;
    }

    @GetMapping("/search")
    public Mono<ResultMovieDto> searchByCriteria(
            @RequestParam String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestHeader("Authorization") String token)
    {
        return omdbApiService.searchByCriteria(query, type, year, page);
    }

}

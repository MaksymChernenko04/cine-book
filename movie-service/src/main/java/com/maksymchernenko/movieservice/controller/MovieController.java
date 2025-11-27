package com.maksymchernenko.movieservice.controller;

import com.maksymchernenko.movieservice.dto.MovieRequest;
import com.maksymchernenko.movieservice.dto.MovieResponse;
import com.maksymchernenko.movieservice.model.Movie;
import com.maksymchernenko.movieservice.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/movies", produces = {"application/json", "application/xml"})
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public Page<MovieResponse> getAllMovies(Pageable pageable,
                                            @RequestParam(required = false) String title) {
        Page<Movie> page;
        if (title != null) {
            page = movieService.getMoviesByTitle(title, pageable);
        } else {
            page = movieService.getAllMovies(pageable);
        }

        return page.map(MovieResponse::fromEntity);
    }

    @GetMapping("/{id}")
    public MovieResponse getMovieById(@PathVariable Integer id) {
        return MovieResponse.fromEntity(movieService.getMovieById(id));
    }

    @PostMapping(consumes = {"application/json", "application/xml"})
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        Movie movie = MovieRequest.toEntity(movieRequest);
        MovieResponse saved = MovieResponse.fromEntity(movieService.createMovie(movie));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Integer id,
                                                     @Valid @RequestBody MovieRequest movieRequest) {
        Movie movie = MovieRequest.toEntity(movieRequest);
        movie.setId(id);

        Movie saved = movieService.updateMovie(movie);

        return ResponseEntity.ok(MovieResponse.fromEntity(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovieById(id);

        return ResponseEntity.noContent().build();
    }
}

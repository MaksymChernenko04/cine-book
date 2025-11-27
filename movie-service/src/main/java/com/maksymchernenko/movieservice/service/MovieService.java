package com.maksymchernenko.movieservice.service;

import com.maksymchernenko.movieservice.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {

    Page<Movie> getMoviesByTitle(String titleSearchText, Pageable pageable);

    Page<Movie> getAllMovies(Pageable pageable);

    Movie getMovieById(Integer id);

    Movie createMovie(Movie movie);

    Movie updateMovie(Movie movie);

    void deleteMovieById(Integer id);
}
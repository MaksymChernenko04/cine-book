package com.maksymchernenko.cinebook.service;

import com.maksymchernenko.cinebook.model.Movie;
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
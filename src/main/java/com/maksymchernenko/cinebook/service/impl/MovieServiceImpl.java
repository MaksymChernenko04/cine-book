package com.maksymchernenko.cinebook.service.impl;

import com.maksymchernenko.cinebook.exception.NotFoundException;
import com.maksymchernenko.cinebook.model.Movie;
import com.maksymchernenko.cinebook.repository.MovieRepository;
import com.maksymchernenko.cinebook.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Page<Movie> getMoviesByTitle(String titleSearchText, Pageable pageable) {
        String text = (titleSearchText == null || titleSearchText.isBlank()) ? "" : titleSearchText.trim();

        return movieRepository.findAllByTitleIsContainingIgnoreCase(text, pageable);
    }

    @Override
    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Movie with id = %d not found", id)));
    }

    @Override
    @Transactional
    public Movie createMovie(Movie movie) {
        movie.setId(null);

        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public Movie updateMovie(Movie movie) {
        if (movie.getId() == null) {
            throw new IllegalArgumentException("Movie id must be provided for update");
        }

        if (!movieRepository.existsById(movie.getId())) {
            throw new NotFoundException(String.format("Movie with id = %d not found", movie.getId()));
        }

        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void deleteMovieById(Integer id) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException(String.format("Movie with id = %d not found", id));
        }

        movieRepository.deleteById(id);
    }
}

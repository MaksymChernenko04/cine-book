package com.maksymchernenko.movieservice.repository;

import com.maksymchernenko.movieservice.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Page<Movie> findAllByTitleIsContainingIgnoreCase(String title, Pageable pageable);
}

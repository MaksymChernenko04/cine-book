package com.maksymchernenko.cinebook.repository;

import com.maksymchernenko.cinebook.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Page<Movie> findAllByTitleIsContainingIgnoreCase(String title, Pageable pageable);
}

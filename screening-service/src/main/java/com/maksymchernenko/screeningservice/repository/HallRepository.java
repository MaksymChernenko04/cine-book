package com.maksymchernenko.screeningservice.repository;

import com.maksymchernenko.screeningservice.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Integer> {
}

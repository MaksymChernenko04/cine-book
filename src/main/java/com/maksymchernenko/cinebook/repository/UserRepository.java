package com.maksymchernenko.cinebook.repository;

import com.maksymchernenko.cinebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

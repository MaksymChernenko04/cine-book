package com.maksymchernenko.userservice.repository;

import com.maksymchernenko.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

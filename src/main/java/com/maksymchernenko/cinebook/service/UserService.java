package com.maksymchernenko.cinebook.service;

import com.maksymchernenko.cinebook.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> getAllUsers(Pageable pageable);

    User getUserById(Integer id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUserById(Integer id);
}

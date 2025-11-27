package com.maksymchernenko.userservice.service;

import com.maksymchernenko.userservice.exception.NotFoundException;
import com.maksymchernenko.userservice.model.User;
import com.maksymchernenko.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %d not found", id)));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        user.setId(null);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        Integer id = user.getId();
        if (id == null) {
            throw new IllegalArgumentException("User id must be provided for update");
        }

        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("User with id = %d not found", id));
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("User with id = %d not found", id));
        }

        userRepository.deleteById(id);
    }
}
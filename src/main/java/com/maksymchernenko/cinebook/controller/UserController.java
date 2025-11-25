package com.maksymchernenko.cinebook.controller;

import com.maksymchernenko.cinebook.dto.UserRequest;
import com.maksymchernenko.cinebook.dto.UserResponse;
import com.maksymchernenko.cinebook.model.User;
import com.maksymchernenko.cinebook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/users", produces = {"application/json", "application/xml"})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable).map(UserResponse::fromEntity);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Integer id) {
        return UserResponse.fromEntity(userService.getUserById(id));
    }

    @PostMapping(consumes = {"application/json", "application/xml"})
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        User user = UserRequest.toEntity(userRequest);
        User saved = userService.createUser(user);
        UserResponse response = UserResponse.fromEntity(saved);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id,
                                                   @Valid @RequestBody UserRequest userRequest) {
        User user = UserRequest.toEntity(userRequest);
        user.setId(id);
        User saved = userService.updateUser(user);

        return ResponseEntity.ok(UserResponse.fromEntity(saved));
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
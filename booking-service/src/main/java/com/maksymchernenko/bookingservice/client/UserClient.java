package com.maksymchernenko.bookingservice.client;

import com.maksymchernenko.bookingservice.client.dto.user.UserDTO;

public interface UserClient {
    UserDTO getUserById(Integer userId);
}
package com.beasties.beasties_backend.application.port;

import com.beasties.beasties_backend.application.dto.AuthResponse;
import com.beasties.beasties_backend.application.dto.RegisterRequest;
import com.beasties.beasties_backend.application.dto.UserDTO;

public interface UserServicePort {
    UserDTO register(RegisterRequest request);
    AuthResponse login(String username, String password);
    UserDTO getById(Long id); // access control in controllers or service
}

package com.beasties.beasties_backend.application.service;


import com.beasties.beasties_backend.application.dto.AuthResponse;
import com.beasties.beasties_backend.application.dto.RegisterRequest;
import com.beasties.beasties_backend.application.dto.UserDTO;
import com.beasties.beasties_backend.application.mapper.UserMapper;
import com.beasties.beasties_backend.application.port.UserServicePort;
import com.beasties.beasties_backend.domain.model.Role;
import com.beasties.beasties_backend.domain.model.User;
import com.beasties.beasties_backend.domain.repository.UserRepository;
import com.beasties.beasties_backend.security.jwt.JwtTokenProvider;
import com.beasties.beasties_backend.web.exception.ResourceAlreadyExistsException;
import com.beasties.beasties_backend.web.exception.ResourceNotFoundException;
import com.beasties.beasties_backend.web.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    public UserDTO register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new ResourceAlreadyExistsException("username");
        }
        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRoles(Set.of(Role.ROLE_USER));
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("invalid credentials");
        }
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        return new AuthResponse(token, "Bearer", jwtTokenProvider.getExpiry());
    }

    @Override
    public UserDTO getById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }
}


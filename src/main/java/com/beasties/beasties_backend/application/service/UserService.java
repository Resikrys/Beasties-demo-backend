package com.beasties.beasties_backend.application.service;

import com.beasties.beasties_backend.application.dto.AuthResponse;
import com.beasties.beasties_backend.application.dto.LoginRequest;
import com.beasties.beasties_backend.application.dto.RegisterRequest;
import com.beasties.beasties_backend.application.dto.UserDTO;
import com.beasties.beasties_backend.application.mapper.UserMapper;
import com.beasties.beasties_backend.domain.model.Role;
import com.beasties.beasties_backend.domain.model.User;
import com.beasties.beasties_backend.domain.repository.UserRepository;
import com.beasties.beasties_backend.shared.exception.UnauthorizedException;
import com.beasties.beasties_backend.security.jwt.JwtTokenProvider;
import com.beasties.beasties_backend.shared.exception.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public UserDTO register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new ResourceAlreadyExistsException("username");
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new ResourceAlreadyExistsException("email");
        }

        var user = User.builder()
                .username(req.username())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .roles(Set.of(Role.ROLE_USER))
                .build();

        var saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public AuthResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Invalid username or password");
        }
        var user = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        var roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        var token = jwtProvider.createToken(user.getUsername(), roles);
        var expiresAt = jwtProvider.getExpiration(token).getTime();
        return new AuthResponse(token, "Bearer", expiresAt);
    }
}


package com.beasties.beasties_backend.auth;

import com.beasties.beasties_backend.auth.dto.JwtResponseDTO;
import com.beasties.beasties_backend.auth.dto.LoginRequestDTO;
import com.beasties.beasties_backend.auth.dto.RegisterRequestDTO;
import com.beasties.beasties_backend.auth.jwt.JwtUtils;
import com.beasties.beasties_backend.user.User;
import com.beasties.beasties_backend.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.beasties.beasties_backend.auth.Role.ROLE_ADMIN;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager,
            UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Registers a new User by default (ROLE_USER).
     */
    public JwtResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Name already in use.");
        }

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String requestedRole = request.getRole();

        if (requestedRole != null && Role.ROLE_ADMIN.name().equals(requestedRole)) {
            user.setRole(Role.ROLE_ADMIN);
        } else {
            user.setRole(Role.ROLE_USER);
        }

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtUtils.generateToken(userDetails);

        return new JwtResponseDTO(jwtToken, user.getUsername(), user.getRole().name());
    }

    /**
     * User authenticate and JWT token generation.
     */
    public JwtResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        User user = userRepository.findByUsername(request.getUsername()).get();
        String jwtToken = jwtUtils.generateToken(userDetails);

        return new JwtResponseDTO(jwtToken, user.getUsername(), user.getRole().name());
    }
}

package com.beasties.beasties_backend.auth;

import com.beasties.beasties_backend.auth.dto.JwtResponseDTO;
import com.beasties.beasties_backend.auth.dto.LoginRequestDTO;
import com.beasties.beasties_backend.auth.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for new Users register.
     * Public (permitAll() in SecurityConfig).
     */
    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint for existent Users Login.
     * Public (permitAll() in SecurityConfig).
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}

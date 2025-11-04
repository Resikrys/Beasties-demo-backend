package com.beasties.beasties_backend.web.controller;

import com.beasties.beasties_backend.application.dto.AuthResponse;
import com.beasties.beasties_backend.application.dto.LoginRequest;
import com.beasties.beasties_backend.application.dto.RegisterRequest;
import com.beasties.beasties_backend.application.dto.UserDTO;
import com.beasties.beasties_backend.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Validated @RequestBody RegisterRequest req) {
        var dto = userService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody LoginRequest req) {
        var auth = userService.login(req);
        return ResponseEntity.ok(auth);
    }
}

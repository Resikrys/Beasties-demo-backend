package com.beasties.beasties_backend.application.dto;

public record AuthResponse(String token, String tokenType, long expiresAt) {}

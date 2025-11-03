package com.beasties.beasties_backend.application.dto;

import java.util.Set;

public record UserDTO(Long id, String username, String email, Set<String> roles) {}

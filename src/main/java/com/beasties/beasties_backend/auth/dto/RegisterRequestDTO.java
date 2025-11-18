package com.beasties.beasties_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Username mandatory")
    private String username;

    @NotBlank(message = "Password mandatory")
    private String password;

    private String role;
}

package com.beasties.beasties_backend.beastie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BeastieRenameDTO {

    @NotBlank(message = "The new name cannot be an empty string.")
    @Size(min = 3, max = 50, message = "The name must contain 3 to 50 characters.")
    private String newName;
}

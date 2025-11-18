package com.beasties.beasties_backend.beastie.dto;

import com.beasties.beasties_backend.beastie.BeastieType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BeastieCreationDTO {

    @NotBlank(message = "Beastie name is mandatory.")
    @Size(min = 3, max = 20, message = "The name must contain from 3 to 20 characters.")
    private String name;

    @NotNull(message = "Beastie type is mandatory.")
    private BeastieType type;

    private String imageUrl;
}

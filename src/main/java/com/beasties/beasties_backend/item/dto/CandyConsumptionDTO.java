package com.beasties.beasties_backend.item.dto;

import com.beasties.beasties_backend.item.CandyType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandyConsumptionDTO {

    @NotNull(message = "The type of candy to be consumed is mandatory.")
    private CandyType candyType;

    @NotNull(message = "The ID of the beastie to be improved is required.")
    private Long beastieId;
}

package com.beasties.beasties_backend.item.dto;

import com.beasties.beasties_backend.item.CandyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryItemDTO {
    private CandyType candyType;
    private String name;
    private int quantity;
    private String improvesStat;
    private int improvementAmount;
}

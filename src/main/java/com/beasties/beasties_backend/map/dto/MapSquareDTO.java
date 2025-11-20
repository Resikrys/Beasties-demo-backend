package com.beasties.beasties_backend.map.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapSquareDTO {
    private int xCoord;
    private int yCoord;
    private Long questId;
    private String questName;
    private int questDifficulty;
}

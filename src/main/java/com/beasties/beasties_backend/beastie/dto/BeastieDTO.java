package com.beasties.beasties_backend.beastie.dto;

import com.beasties.beasties_backend.beastie.BeastieType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeastieDTO {
    private Long id;
    private String name;
    private int level;
    private BeastieType type;
    private String imageUrl;
    private int dexterity;
    private int strength;
    private int intelligence;
    private int stamina;
    private int maxStamina;
    private boolean isInTeam;
    private boolean isSad;
    private Long ownerId;
}

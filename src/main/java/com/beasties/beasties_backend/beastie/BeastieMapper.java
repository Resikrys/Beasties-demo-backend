package com.beasties.beasties_backend.beastie;

import com.beasties.beasties_backend.beastie.dto.BeastieDTO;
import org.springframework.stereotype.Component;

@Component
public class BeastieMapper {

    public BeastieDTO toDTO(Beastie beastie) {
        int expRequired = beastie.getLevel() * 100;

        return BeastieDTO.builder()
                .id(beastie.getId())
                .name(beastie.getName())
                .level(beastie.getLevel())
                .type(beastie.getType())
                .imageUrl(beastie.getImageUrl())
                .dexterity(beastie.getDexterity())
                .strength(beastie.getStrength())
                .intelligence(beastie.getIntelligence())
                .stamina(beastie.getStamina())
                .maxStamina(beastie.getMaxStamina())
                .isInTeam(beastie.isInTeam())
                .isSad(beastie.isSad())
                .ownerId(beastie.getOwner().getId())
                .experience(beastie.getExperience())
                .experienceRequired(expRequired)
                .build();
    }
}

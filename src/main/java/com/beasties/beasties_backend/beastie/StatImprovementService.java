package com.beasties.beasties_backend.beastie;

import com.beasties.beasties_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatImprovementService {

    private final BeastieRepository beastieRepository;

    public StatImprovementService(BeastieRepository beastieRepository) {
        this.beastieRepository = beastieRepository;
    }

    @Transactional
    public Beastie improveStat(Long beastieId, Stat stat, int amount) {
        Beastie beastie = beastieRepository.findById(beastieId)
                .orElseThrow(() -> new ResourceNotFoundException("Beastie", "id", beastieId));

        switch (stat) {
            case STRENGTH:
                beastie.setStrength(beastie.getStrength() + amount);
                break;
            case DEXTERITY:
                beastie.setDexterity(beastie.getDexterity() + amount);
                break;
            case INTELLIGENCE:
                beastie.setIntelligence(beastie.getIntelligence() + amount);
                break;
            default:
                throw new IllegalArgumentException("Stat invalid for improvement: " + stat);
        }

        return beastieRepository.save(beastie);
    }
}

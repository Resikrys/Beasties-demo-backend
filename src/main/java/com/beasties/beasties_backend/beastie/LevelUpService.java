package com.beasties.beasties_backend.beastie;

import com.beasties.beasties_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

@Service
public class LevelUpService {

    private final BeastieRepository beastieRepository;
    private final Random random = new Random();

    public LevelUpService(BeastieRepository beastieRepository) {
        this.beastieRepository = beastieRepository;
    }

    /**
     * Check the creature's EXP. If it's high enough, apply the level-up.
     * and stat boosts. You can level up multiple times in one call.
     */
    @Transactional
    public boolean checkForLevelUp(Long beastieId) {
        Beastie beastie = beastieRepository.findById(beastieId)
                .orElseThrow(() -> new ResourceNotFoundException("Beastie", "id", beastieId));

        boolean leveledUp = false;

        while (beastie.getLevel() < ExperienceCurve.MAX_LEVEL) {
            int expToNextLevel = ExperienceCurve.getExpToNextLevel(beastie.getLevel());

            if (beastie.getExperience() >= expToNextLevel) {
                beastie.setLevel(beastie.getLevel() + 1);
                applyStatGrowth(beastie);
                leveledUp = true;
            } else {
                break;
            }
        }

        if (leveledUp) {
            beastieRepository.save(beastie);
        }

        return leveledUp;
    }

    /**
     * Applies base stat increase upon leveling up (fixed + random).
     */
    private void applyStatGrowth(Beastie beastie) {
        Stat mainStat = beastie.getType().getMainStat();
        increaseStat(beastie, mainStat, 2);

        Stat secondaryStat = switch (mainStat) {
            case STRENGTH -> random.nextBoolean() ? Stat.DEXTERITY : Stat.INTELLIGENCE;
            case DEXTERITY -> random.nextBoolean() ? Stat.STRENGTH : Stat.INTELLIGENCE;
            case INTELLIGENCE -> random.nextBoolean() ? Stat.STRENGTH : Stat.DEXTERITY;
        };
        increaseStat(beastie, secondaryStat, 1);

        beastie.setMaxStamina(beastie.getMaxStamina() + 5);
        beastie.setStamina(beastie.getMaxStamina());
    }

    private void increaseStat(Beastie beastie, Stat stat, int amount) {
        switch (stat) {
            case STRENGTH -> beastie.setStrength(beastie.getStrength() + amount);
            case DEXTERITY -> beastie.setDexterity(beastie.getDexterity() + amount);
            case INTELLIGENCE -> beastie.setIntelligence(beastie.getIntelligence() + amount);
        }
    }
}

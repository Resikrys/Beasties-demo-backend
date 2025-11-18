package com.beasties.beasties_backend.beastie;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StaminaRecoveryScheduler {

    private final BeastieRepository beastieRepository;

    public StaminaRecoveryScheduler(BeastieRepository beastieRepository) {
        this.beastieRepository = beastieRepository;
    }

    /**
     * Recover 1 stamina point every 10 seconds for all beasties that are below max stamina.
     */
    @Scheduled(fixedRate = 10000)
    public void recoverStamina() {
        List<Beastie> allBeasties = beastieRepository.findAll();

        for (Beastie beastie : allBeasties) {
            if (beastie.getStamina() < beastie.getMaxStamina()) {
                int newStamina = Math.min(beastie.getStamina() + 1, beastie.getMaxStamina());
                beastie.setStamina(newStamina);
            }
        }

        beastieRepository.saveAll(allBeasties);
    }
}

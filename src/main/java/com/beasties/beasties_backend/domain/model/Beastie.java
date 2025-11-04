package com.beasties.beasties_backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Entity
@Table(name = "beasties")
@Getter
@Setter
@NoArgsConstructor
public class Beastie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int level = 1;
    @Enumerated(EnumType.STRING)
    private BeastieType type; // EXPLORER, FIGHTER, SAGE
    private int strength;
    private int dexterity;
    private int intelligence;
    private int stamina;
    private boolean inTeam;
    private Long ownerId;
    private Instant sadUntil;

    public void levelUp() {
        this.level++;
        // regla: depending on type, preferente +2, secundario +1, tercero +0
        switch(type) {
            case FIGHTER -> { strength += 2; intelligence += 1; dexterity += 0; }
            case SAGE -> { intelligence += 2; dexterity += 1; strength += 0; }
            case EXPLORER -> { dexterity += 2; strength += 1; intelligence += 0; }
        }
    }

    public void eatCandy(CandyType candy) {
        switch(candy) {
            case STRAWBERRY -> strength += 1; break;
            case BANANA -> dexterity += 1; break;
            case RASPBERRY -> intelligence += 1; break;
        }
    }
}

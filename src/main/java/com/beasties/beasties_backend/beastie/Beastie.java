package com.beasties.beasties_backend.beastie;

import com.beasties.beasties_backend.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beastie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int level = 1;

    private int dexterity = 1;
    private int strength = 1;
    private int intelligence = 1;

    private int stamina = 100;
    private int maxStamina = 100;
    private int experience = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeastieType type;

    @Column(length = 255)
    private String imageUrl;

    private boolean isInTeam = false;
    private boolean isSad = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public Beastie(String name, BeastieType type, User owner) {
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public int getStatValue(Stat stat) {
        return switch (stat) {
            case DEXTERITY -> this.dexterity;
            case STRENGTH -> this.strength;
            case INTELLIGENCE -> this.intelligence;
        };
    }
}

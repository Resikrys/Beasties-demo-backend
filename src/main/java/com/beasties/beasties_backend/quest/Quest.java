package com.beasties.beasties_backend.quest;

import com.beasties.beasties_backend.beastie.Stat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestType type;

    @Column(nullable = false)
    private int difficultyLevel;

    @Column(nullable = false)
    private Stat requiredStat;

    @Column(nullable = false)
    private int baseStatRequirement;

    private int minExpReward;
    private int maxExpReward;
    private int durationMinutes;
}

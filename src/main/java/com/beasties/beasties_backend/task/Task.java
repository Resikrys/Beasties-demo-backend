package com.beasties.beasties_backend.task;

import com.beasties.beasties_backend.beastie.Stat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stat statToImprove;

    @Column(nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private int staminaCost = 10;

    public Task(String name, String description, Stat statToImprove, int durationMinutes) {
        this.name = name;
        this.description = description;
        this.statToImprove = statToImprove;
        this.durationMinutes = durationMinutes;
        this.staminaCost = 10;
    }
}

package com.beasties.beasties_backend.quest;

import com.beasties.beasties_backend.beastie.Beastie;
import com.beasties.beasties_backend.map.MapSquare;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ActiveQuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beastie_id", unique = true, nullable = false)
    private Beastie beastie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest questTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_square_id", nullable = false)
    private MapSquare mapSquare;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int beastieStatValue;

    @Column(nullable = false)
    private int requiredStatValue;
}
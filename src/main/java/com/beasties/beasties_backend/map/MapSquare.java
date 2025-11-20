package com.beasties.beasties_backend.map;

import com.beasties.beasties_backend.quest.Quest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapSquare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int XCoord;

    @Column(nullable = false)
    private int YCoord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    public MapSquare(int xCoord, int yCoord) {
        this.XCoord = xCoord;
        this.YCoord = yCoord;
    }
}

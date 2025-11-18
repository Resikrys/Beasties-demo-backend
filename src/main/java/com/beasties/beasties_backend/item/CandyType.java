package com.beasties.beasties_backend.item;

import com.beasties.beasties_backend.beastie.Stat;
import lombok.Getter;

@Getter
public enum CandyType {
    STRAWBERRY("Strawberry", Stat.STRENGTH, 1),
    BANANA("Banana", Stat.DEXTERITY, 1),
    RASPBERRY("Raspberry", Stat.INTELLIGENCE, 1);

    private final String name;
    private final Stat statToImprove;
    private final int improvementAmount;

    CandyType(String name, Stat statToImprove, int improvementAmount) {
        this.name = name;
        this.statToImprove = statToImprove;
        this.improvementAmount = improvementAmount;
    }
}

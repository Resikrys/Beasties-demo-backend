package com.beasties.beasties_backend.beastie;

public enum BeastieType {
    EXPLORER(Stat.DEXTERITY),
    FIGHTER(Stat.STRENGTH),
    SAGE(Stat.INTELLIGENCE);

    private final Stat mainStat;

    BeastieType(Stat mainStat) {
        this.mainStat = mainStat;
    }

    public Stat getMainStat() {
        return mainStat;
    }
}

package com.beasties.beasties_backend.beastie;

public class ExperienceCurve {

    private static final int BASE_EXP = 100;
    private static final double GROWTH_FACTOR = 1.5;
    public static final int MAX_LEVEL = 50;

    /**
     * Calculate the total accumulated experience needed to reach a level.
     */
    public static int getExpRequiredForLevel(int level) {
        if (level >= MAX_LEVEL) {
            return Integer.MAX_VALUE;
        }
        if (level <= 1) {
            return 0;
        }
        return (int) (BASE_EXP * Math.pow(level - 1, GROWTH_FACTOR));
    }

    /**
     * Calculate the total experience needed to reach the current level + 1.
     */
    public static int getExpToNextLevel(int currentLevel) {
        return getExpRequiredForLevel(currentLevel + 1);
    }
}


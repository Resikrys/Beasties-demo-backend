package com.beasties.beasties_backend.quest.dto;

import com.beasties.beasties_backend.beastie.Stat;
import com.beasties.beasties_backend.quest.QuestType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class QuestDTO {

    private Long id;

    @NotBlank(message = "Quest name is mandatory.")
    private String name;

    @NotBlank(message = "The quest description is mandatory.")
    private String description;

    @NotNull(message = "The quest type is mandatory.")
    private QuestType type;

    @Min(value = 1, message = "The minimum difficulty is 1.")
    @Max(value = 5, message = "The maximum difficulty is 5")
    private int difficultyLevel;

    @NotNull(message = "The required stat is mandatory.")
    private Stat requiredStat;

    @Min(value = 1, message = "The minimum base stat requirement is 1.")
    private int baseStatRequirement;

    @Min(value = 0, message = "The minimum EXP reward cannot be negative.")
    private int minExpReward;

    @Min(value = 0, message = "The maximum EXP reward cannot be negative.")
    private int maxExpReward;

    @Min(value = 1, message = "The minimum duration of the Quest is 1 minute.")
    private int durationMinutes;
}

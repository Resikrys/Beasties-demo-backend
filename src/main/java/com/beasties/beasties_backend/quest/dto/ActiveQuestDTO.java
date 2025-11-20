package com.beasties.beasties_backend.quest.dto;

import com.beasties.beasties_backend.beastie.Stat;
import com.beasties.beasties_backend.quest.QuestType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActiveQuestDTO {
    private Long id;
    private Long beastieId;
    private String beastieName;
    private Long questId;
    private String questName;
    private QuestType questType;
    private Stat requiredStat;
    private int difficultyLevel;
    private LocalDateTime endTime;
    private int durationMinutes;
    private long remainingSeconds;
    private int x;
    private int y;
}

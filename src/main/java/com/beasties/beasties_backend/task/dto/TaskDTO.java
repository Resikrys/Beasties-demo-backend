package com.beasties.beasties_backend.task.dto;

import com.beasties.beasties_backend.beastie.Stat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private Stat statToImprove;
    private int durationMinutes;
    private int staminaCost;
}

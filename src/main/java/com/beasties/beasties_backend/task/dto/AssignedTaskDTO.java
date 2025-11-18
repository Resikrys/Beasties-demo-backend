package com.beasties.beasties_backend.task.dto;

import com.beasties.beasties_backend.beastie.Stat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AssignedTaskDTO {
    private Long id;
    private Long beastieId;
    private Long taskId;
    private String taskName;
    private Stat statToImprove;
    private LocalDateTime endTime;
    private long remainingSeconds;
}

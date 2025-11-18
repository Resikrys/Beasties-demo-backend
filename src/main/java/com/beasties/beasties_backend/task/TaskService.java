package com.beasties.beasties_backend.task;

import com.beasties.beasties_backend.beastie.Beastie;
import com.beasties.beasties_backend.beastie.BeastieRepository;
import com.beasties.beasties_backend.beastie.StatImprovementService;
import com.beasties.beasties_backend.exception.ResourceNotFoundException;
import com.beasties.beasties_backend.task.dto.AssignedTaskDTO;
import com.beasties.beasties_backend.task.dto.TaskDTO;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final BeastieRepository beastieRepository;
    private final AssignedTaskRepository assignedTaskRepository;
    private final StatImprovementService statImprovementService;

    public TaskService(
            TaskRepository taskRepository,
            BeastieRepository beastieRepository,
            AssignedTaskRepository assignedTaskRepository,
            StatImprovementService statImprovementService) {
        this.taskRepository = taskRepository;
        this.beastieRepository = beastieRepository;
        this.assignedTaskRepository = assignedTaskRepository;
        this.statImprovementService = statImprovementService;
    }

    /**
     * getAll available Tasks.
     */
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(t -> TaskDTO.builder()
                        .id(t.getId()).name(t.getName()).description(t.getDescription())
                        .statToImprove(t.getStatToImprove()).durationMinutes(t.getDurationMinutes())
                        .staminaCost(t.getStaminaCost())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * It obtains all assigned tasks from the user's creatures.
     */
    public List<AssignedTaskDTO> getUserAssignedTasks(Long userId) {
        List<Beastie> userBeasties = beastieRepository.findByOwnerId(userId);
        List<Long> beastieIds = userBeasties.stream().map(Beastie::getId).collect(Collectors.toList());

        List<AssignedTask> assignedTasks = assignedTaskRepository.findByBeastieIdIn(beastieIds);

        return assignedTasks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Assign a task to a creature.
     */
    public AssignedTaskDTO assignTask(Long beastieId, Long taskId, Long userId) {
        Beastie beastie = beastieRepository.findByIdAndOwnerId(beastieId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Beastie", "id", beastieId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        if (assignedTaskRepository.existsByBeastieId(beastieId)) {
            throw new IllegalStateException("This creature already has an assigned task..");
        }

        if (beastie.getStamina() < task.getStaminaCost()) {
            throw new IllegalStateException("The creature does not have enough stamina for this task.");
        }

        beastie.setStamina(beastie.getStamina() - task.getStaminaCost());
        beastieRepository.save(beastie);

        LocalDateTime endTime = LocalDateTime.now().plusMinutes(task.getDurationMinutes());

        AssignedTask assignedTask = new AssignedTask();
        assignedTask.setBeastie(beastie);
        assignedTask.setTask(task);
        assignedTask.setEndTime(endTime);

        AssignedTask savedTask = assignedTaskRepository.save(assignedTask);

        return toDTO(savedTask);
    }

    /**
     * Check if a task has finished and apply the stat improvement.
     */
    public AssignedTaskDTO checkAndCompleteTask(Long beastieId, Long userId) {
        AssignedTask assignedTask = assignedTaskRepository.findByBeastieId(beastieId)
                .orElseThrow(() -> new ResourceNotFoundException("AssignedTask", "beastieId", beastieId));

        if (!assignedTask.getBeastie().getOwner().getId().equals(userId)) {
            throw new SecurityException("Access denied. The creature does not belong to the user.");
        }

        if (assignedTask.getEndTime().isAfter(LocalDateTime.now())) {
            return toDTO(assignedTask);
        }

        statImprovementService.improveStat(
                assignedTask.getBeastie().getId(),
                assignedTask.getTask().getStatToImprove(),
                1
        );

        assignedTaskRepository.delete(assignedTask);

        return AssignedTaskDTO.builder()
                .beastieId(assignedTask.getBeastie().getId())
                .taskName(assignedTask.getTask().getName())
                .statToImprove(assignedTask.getTask().getStatToImprove())
                .remainingSeconds(0)
                .build();
    }

    /**
     * Internal mapper to calculate remaining time.
     */
    private AssignedTaskDTO toDTO(AssignedTask assignedTask) {
        long remainingSeconds = Duration.between(LocalDateTime.now(), assignedTask.getEndTime()).getSeconds();
        if (remainingSeconds < 0) remainingSeconds = 0;

        return AssignedTaskDTO.builder()
                .id(assignedTask.getId())
                .beastieId(assignedTask.getBeastie().getId())
                .taskId(assignedTask.getTask().getId())
                .taskName(assignedTask.getTask().getName())
                .statToImprove(assignedTask.getTask().getStatToImprove())
                .endTime(assignedTask.getEndTime())
                .remainingSeconds(remainingSeconds)
                .build();
    }
}

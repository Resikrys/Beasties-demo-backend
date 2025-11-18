package com.beasties.beasties_backend.task;

import com.beasties.beasties_backend.task.dto.AssignedTaskDTO;
import com.beasties.beasties_backend.task.dto.TaskDTO;
import com.beasties.beasties_backend.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    /**
     * getAll tasks available.
     */
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * Retrieves all tasks assigned to the current user.
     */
    @GetMapping("/assigned")
    public ResponseEntity<List<AssignedTaskDTO>> getUserAssignedTasks(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        return ResponseEntity.ok(taskService.getUserAssignedTasks(userId));
    }

    /**
     * Assign a task to a user creature.
     */
    @PostMapping("/assign/{beastieId}/{taskId}")
    public ResponseEntity<AssignedTaskDTO> assignTask(
            @PathVariable Long beastieId,
            @PathVariable Long taskId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        return new ResponseEntity<>(taskService.assignTask(beastieId, taskId, userId), HttpStatus.CREATED);
    }

    /**
     * Check the status of a creature's task and complete it if it has finished.
     */
    @PatchMapping("/complete/{beastieId}")
    public ResponseEntity<AssignedTaskDTO> checkAndCompleteTask(
            @PathVariable Long beastieId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        return ResponseEntity.ok(taskService.checkAndCompleteTask(beastieId, userId));
    }
}

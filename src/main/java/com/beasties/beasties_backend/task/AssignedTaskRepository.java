package com.beasties.beasties_backend.task;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignedTaskRepository extends JpaRepository<AssignedTask, Long> {
    Optional<AssignedTask> findByBeastieId(Long beastieId);
    boolean existsByBeastieId(Long beastieId);
    List<AssignedTask> findByBeastieIdIn(List<Long> beastieIds);
}

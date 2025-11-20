package com.beasties.beasties_backend.quest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActiveQuestRepository extends JpaRepository<ActiveQuest, Long> {
    Optional<ActiveQuest> findByBeastieId(Long beastieId);
    boolean existsByBeastieId(Long beastieId);
    List<ActiveQuest> findByBeastieIdIn(List<Long> beastieIds);
}

package com.beasties.beasties_backend.beastie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeastieRepository extends JpaRepository<Beastie, Long> {

    List<Beastie> findByOwnerId(Long ownerId);

    Optional<Beastie> findByIdAndOwnerId(Long beastieId, Long ownerId);

    long countByOwnerIdAndIsInTeamTrue(Long ownerId);
}

package com.beasties.beasties_backend.map;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MapSquareRepository extends JpaRepository<MapSquare, Long> {
    Optional<MapSquare> findByXCoordAndYCoord(int x, int y);
}

package com.beasties.beasties_backend.item;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    /**
     * Search all items in user inventory.
     */
    List<InventoryItem> findByOwnerId(Long ownerId);

    /**
     * Search a specific item (type candy) from a user.
     */
    Optional<InventoryItem> findByOwnerIdAndCandyType(Long ownerId, CandyType candyType);
}


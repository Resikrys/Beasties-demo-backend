package com.beasties.beasties_backend.item;

import com.beasties.beasties_backend.beastie.Beastie;
import com.beasties.beasties_backend.beastie.BeastieRepository;
import com.beasties.beasties_backend.beastie.StatImprovementService;
import com.beasties.beasties_backend.exception.ResourceNotFoundException;
import com.beasties.beasties_backend.item.dto.CandyConsumptionDTO;
import com.beasties.beasties_backend.item.dto.InventoryItemDTO;
import com.beasties.beasties_backend.user.User;
import com.beasties.beasties_backend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final StatImprovementService statImprovementService;
    private final BeastieRepository beastieRepository;
    private final UserRepository userRepository;

    public ItemService(
            InventoryItemRepository inventoryItemRepository,
            StatImprovementService statImprovementService,
            BeastieRepository beastieRepository,
            UserRepository userRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.statImprovementService = statImprovementService;
        this.beastieRepository = beastieRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lists all items in user's inventory
     */
    public List<InventoryItemDTO> getMyInventory(Long userId) {
        return inventoryItemRepository.findByOwnerId(userId).stream()
                .map(item -> InventoryItemDTO.builder()
                        .candyType(item.getCandyType())
                        .name(item.getCandyType().getName())
                        .quantity(item.getQuantity())
                        .improvesStat(item.getCandyType().getStatToImprove().name())
                        .improvementAmount(item.getCandyType().getImprovementAmount())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * [CORE] Adds a quantity of a certain type of candy to the user's inventory.
     * Used after successfully completing a Quest.
     */
    @Transactional
    public void addCandy(Long userId, CandyType candyType, int amount) {
        if (amount <= 0) return;

        User user = userRepository.getReferenceById(userId);

        InventoryItem item = inventoryItemRepository
                .findByOwnerIdAndCandyType(userId, candyType)
                .orElseGet(() -> {
                    InventoryItem newItem = new InventoryItem();
                    newItem.setOwner(user);
                    newItem.setCandyType(candyType);
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + amount);
        inventoryItemRepository.save(item);
    }

    /**
     * [CORE] Consumes a candy to improve a beastie.
     */
    @Transactional
    public Beastie consumeCandy(CandyConsumptionDTO dto, Long userId) {
        Long beastieId = dto.getBeastieId();
        CandyType candyType = dto.getCandyType();


        Beastie beastie = beastieRepository.findByIdAndOwnerId(beastieId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Beastie", "id", beastieId));

        InventoryItem inventoryItem = inventoryItemRepository
                .findByOwnerIdAndCandyType(userId, candyType)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "candyType", candyType.name()));

        if (inventoryItem.getQuantity() < 1) {
            throw new IllegalStateException("You don't have any " + candyType.getName() + " to consume.");
        }

        inventoryItem.setQuantity(inventoryItem.getQuantity() - 1);
        inventoryItemRepository.save(inventoryItem);

        return statImprovementService.improveStat(
                beastieId,
                candyType.getStatToImprove(),
                candyType.getImprovementAmount()
        );
    }
}

package com.beasties.beasties_backend.item;

import com.beasties.beasties_backend.beastie.Beastie;
import com.beasties.beasties_backend.item.dto.CandyConsumptionDTO;
import com.beasties.beasties_backend.item.dto.InventoryItemDTO;
import com.beasties.beasties_backend.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    /**
     * Shows user inventory.
     */
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getMyInventory(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        return ResponseEntity.ok(itemService.getMyInventory(userId));
    }

    /**
     * Consumes a candy to improve a beastie.
     */
    @PatchMapping("/consume")
    public ResponseEntity<String> consumeCandy(
            @Valid @RequestBody CandyConsumptionDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        Beastie updatedBeastie = itemService.consumeCandy(dto, userId);

        return ResponseEntity.ok(String.format(
                "¡Success! Consumed %s. Stat %s of %s has been updated %d.",
                dto.getCandyType().getName(),
                dto.getCandyType().getStatToImprove().name(),
                updatedBeastie.getName(),
                updatedBeastie.getStatValue(dto.getCandyType().getStatToImprove())
        ));
    }
}

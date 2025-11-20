package com.beasties.beasties_backend.map;

import com.beasties.beasties_backend.map.dto.MapSquareDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/map")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * [USER/ADMIN] View map.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<MapSquareDTO>> getCurrentMap() {
        return ResponseEntity.ok(mapService.getCurrentMap());
    }

    /**
     * [ADMIN] Regenerate the map with new random quests.
     */
    @PostMapping("/randomize")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MapSquareDTO>> randomizeMap() {
        return ResponseEntity.ok(mapService.randomizeMap());
    }

    /**
     * [ADMIN] Remove a quest from a specific box.
     */
    @DeleteMapping("/clear/{x}/{y}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MapSquareDTO> clearSquare(@PathVariable int x, @PathVariable int y) {
        return ResponseEntity.ok(mapService.clearSquare(x, y));
    }
}

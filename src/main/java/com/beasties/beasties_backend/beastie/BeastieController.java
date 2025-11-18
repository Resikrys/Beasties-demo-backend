package com.beasties.beasties_backend.beastie;

import com.beasties.beasties_backend.beastie.dto.BeastieCreationDTO;
import com.beasties.beasties_backend.beastie.dto.BeastieDTO;
import com.beasties.beasties_backend.beastie.dto.BeastieRenameDTO;
import com.beasties.beasties_backend.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BeastieController {

    private final BeastieService beastieService;
    private final UserService userService;

    public BeastieController(BeastieService beastieService, UserService userService) {
        this.beastieService = beastieService;
        this.userService = userService;
    }

    /**
     * [ROLE_USER, ROLE_ADMIN] - Adopt a new Beastie.
     */
    @PostMapping("/beasties")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BeastieDTO> adoptBeastie(
            @Valid @RequestBody BeastieCreationDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        return new ResponseEntity<>(beastieService.adoptBeastie(dto, userId), HttpStatus.CREATED);
    }

    /**
     * [ROLE_USER, ROLE_ADMIN] - getAll Beasties from a logged-in User.
     */
    @GetMapping("/beasties")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<BeastieDTO>> getMyBeasties(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        return ResponseEntity.ok(beastieService.getMyBeasties(userId));
    }

    /**
     * [ROLE_USER, ROLE_ADMIN] - Release (delete) a Beastie.
     */
    @DeleteMapping("/beasties/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> releaseBeastie(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        beastieService.releaseBeastie(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * [ROLE_USER, ROLE_ADMIN] - Assign/unassign a creature to the team of 5.
     */
    @PatchMapping("/beasties/{id}/team")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BeastieDTO> toggleTeam(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        return ResponseEntity.ok(beastieService.toggleTeam(id, userId));
    }

    // --- Endpoints ADMIN ---

    /**
     * [ROLE_ADMIN] - getAll existing Beasties.
     */
    @GetMapping("/admin/beasties")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BeastieDTO>> getAllBeastiesForAdmin() {
        return ResponseEntity.ok(beastieService.getAllBeastiesForAdmin());
    }
    /**
     * [ROLE_ADMIN] - Rename a Beastie (solo para admins).
     * @PreAuthorize("hasRole('ADMIN')") protects this endpoint.
     */
    @PatchMapping("/admin/beasties/{id}/rename")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BeastieDTO> renameBeastie(
            @PathVariable Long id,
            @Valid @RequestBody BeastieRenameDTO dto
    ) {
        return ResponseEntity.ok(beastieService.renameBeastie(id, dto.getNewName()));
    }
}

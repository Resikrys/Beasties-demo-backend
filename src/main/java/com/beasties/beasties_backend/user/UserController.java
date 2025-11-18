package com.beasties.beasties_backend.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserManagerService userManagerService;

    public UserController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    /**
     * Endpoint: GET /api/v1/admin/users
     * List all existing users.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userManagerService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint: DELETE /api/v1/admin/users/{id}
     * deletes user by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userManagerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

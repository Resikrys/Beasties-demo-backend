package com.beasties.beasties_backend.quest;

import com.beasties.beasties_backend.quest.dto.QuestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/quests")
@PreAuthorize("hasRole('ADMIN')")
public class QuestManagementController {

    private final QuestManagementService questManagementService;

    public QuestManagementController(QuestManagementService questManagementService) {
        this.questManagementService = questManagementService;
    }

    @PostMapping
    public ResponseEntity<QuestDTO> createQuest(@Valid @RequestBody QuestDTO dto) {
        return new ResponseEntity<>(questManagementService.createQuest(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuestDTO>> getAllQuests() {
        return ResponseEntity.ok(questManagementService.getAllQuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestDTO> getQuestById(@PathVariable Long id) {
        return ResponseEntity.ok(questManagementService.getQuestById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestDTO> updateQuest(@PathVariable Long id, @Valid @RequestBody QuestDTO dto) {
        return ResponseEntity.ok(questManagementService.updateQuest(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable Long id) {
        questManagementService.deleteQuest(id);
        return ResponseEntity.noContent().build();
    }
}

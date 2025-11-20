package com.beasties.beasties_backend.quest;

import com.beasties.beasties_backend.quest.dto.ActiveQuestDTO;
import com.beasties.beasties_backend.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quests")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class QuestController {

    private final QuestService questService;
    private final UserService userService;

    public QuestController(QuestService questService, UserService userService) {
        this.questService = questService;
        this.userService = userService;
    }

    /**
     * [USER/ADMIN] Send a beastie to the quest.
     */
    @PostMapping("/start/{beastieId}/{x}/{y}")
    public ResponseEntity<String> startQuest(
            @PathVariable Long beastieId,
            @PathVariable int x,
            @PathVariable int y,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        questService.startQuest(beastieId, x, y, userId);
        return new ResponseEntity<>("Quest initialized.", HttpStatus.OK);
    }

    /**
     * [USER/ADMIN] Check and complete the Quest if it is finished.
     */
    @PatchMapping("/complete/{beastieId}")
    public ResponseEntity<String> completeQuest(
            @PathVariable Long beastieId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        String result = questService.completeQuest(beastieId, userId);
        return ResponseEntity.ok(result);
    }

    /**
     * [USER/ADMIN] Get all user's active quests.
     */
    @GetMapping("/active")
    public ResponseEntity<List<ActiveQuestDTO>> getActiveQuests(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = userService.findIdByUsername(userDetails.getUsername());
        List<ActiveQuestDTO> activeQuests = questService.getUserActiveQuests(userId);
        return ResponseEntity.ok(activeQuests);
    }
}

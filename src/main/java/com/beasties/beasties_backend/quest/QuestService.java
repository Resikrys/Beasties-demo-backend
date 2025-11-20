package com.beasties.beasties_backend.quest;

import com.beasties.beasties_backend.beastie.*;
import com.beasties.beasties_backend.exception.ResourceNotFoundException;
import com.beasties.beasties_backend.item.CandyType;
import com.beasties.beasties_backend.item.ItemService;
import com.beasties.beasties_backend.map.MapSquare;
import com.beasties.beasties_backend.map.MapSquareRepository;
import com.beasties.beasties_backend.quest.dto.ActiveQuestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class QuestService {

    private final BeastieRepository beastieRepository;
    private final MapSquareRepository mapSquareRepository;
    private final ActiveQuestRepository activeQuestRepository;
    private final ItemService itemService;
    private final LevelUpService levelUpService;
    private final Random random = new Random();

    public QuestService(
            BeastieRepository beastieRepository,
            MapSquareRepository mapSquareRepository,
            ActiveQuestRepository activeQuestRepository,
            ItemService itemService,
            LevelUpService levelUpService) {
        this.beastieRepository = beastieRepository;
        this.mapSquareRepository = mapSquareRepository;
        this.activeQuestRepository = activeQuestRepository;
        this.itemService = itemService;
        this.levelUpService = levelUpService;
    }

    /**
     * Send a creature from the team on a Quest in a map tile.
     */
    @Transactional
    public void startQuest(Long beastieId, int x, int y, Long userId) {
        Beastie beastie = beastieRepository.findByIdAndOwnerId(beastieId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Beastie", "id", beastieId));

        MapSquare square = mapSquareRepository.findByXCoordAndYCoord(x, y)
                .orElseThrow(() -> new ResourceNotFoundException("Map square", "coordenades", x + "," + y));

        Quest questTemplate = Optional.ofNullable(square.getQuest())
                .orElseThrow(() -> new IllegalStateException("This map square is empty."));

        if (!beastie.isInTeam()) throw new IllegalStateException("Beastie must be in your team.");
        if (beastie.isSad()) throw new IllegalStateException("Beastie is sad and must rest.");
        if (activeQuestRepository.existsByBeastieId(beastieId)) throw new IllegalStateException("Beastie is already in a quest.");
        if (getBeastieTypeForQuest(questTemplate.getType()) != beastie.getType()) {
            throw new IllegalArgumentException("Beastie type incorrect. Required: " + questTemplate.getType());
        }

        int requiredStatValue = questTemplate.getBaseStatRequirement() * questTemplate.getDifficultyLevel();
        int beastieStatValue = getBeastieStatValue(beastie, questTemplate.getRequiredStat());

        ActiveQuest activeQuest = new ActiveQuest();
        activeQuest.setBeastie(beastie);
        activeQuest.setQuestTemplate(questTemplate);
        activeQuest.setMapSquare(square);
        activeQuest.setEndTime(LocalDateTime.now().plusMinutes(questTemplate.getDurationMinutes()));
        activeQuest.setBeastieStatValue(beastieStatValue);
        activeQuest.setRequiredStatValue(requiredStatValue);

        activeQuestRepository.save(activeQuest);

        beastie.setStamina(beastie.getStamina() - 10);
        beastieRepository.save(beastie);
    }

    /**
     * Check and complete the Quest, calculating success or failure.
     */
    @Transactional
    public String completeQuest(Long beastieId, Long userId) {
        ActiveQuest activeQuest = activeQuestRepository.findByBeastieId(beastieId)
                .orElseThrow(() -> new ResourceNotFoundException("ActiveQuest", "beastieId", beastieId));

        if (!activeQuest.getBeastie().getOwner().getId().equals(userId)) {
            throw new SecurityException("Access denied.");
        }

        if (activeQuest.getEndTime().isAfter(LocalDateTime.now())) {
            return "IN A QUEST: quest yet not finished.";
        }

        boolean success = calculateSuccess(activeQuest);

        String result = success ? handleSuccess(activeQuest) : handleFailure(activeQuest);

        activeQuestRepository.delete(activeQuest);

        activeQuest.getMapSquare().setQuest(null);
        mapSquareRepository.save(activeQuest.getMapSquare());

        return result;
    }

    /**
     * Get all user's active quests.
     */
    public List<ActiveQuestDTO> getUserActiveQuests(Long userId) {
        List<Beastie> userBeasties = beastieRepository.findByOwnerId(userId);
        List<Long> beastieIds = userBeasties.stream().map(Beastie::getId).collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();
        List<ActiveQuest> activeQuests = activeQuestRepository.findByBeastieIdIn(beastieIds);

        return activeQuests.stream().map(aq -> mapToDTO(aq, now)).collect(Collectors.toList());
    }

    private ActiveQuestDTO mapToDTO(ActiveQuest activeQuest, LocalDateTime now) {
        ActiveQuestDTO dto = new ActiveQuestDTO();
        dto.setId(activeQuest.getId());
        dto.setBeastieId(activeQuest.getBeastie().getId());
        dto.setBeastieName(activeQuest.getBeastie().getName());
        dto.setQuestId(activeQuest.getQuestTemplate().getId());
        dto.setQuestName(activeQuest.getQuestTemplate().getName());
        dto.setQuestType(activeQuest.getQuestTemplate().getType());
        dto.setRequiredStat(activeQuest.getQuestTemplate().getRequiredStat());
        dto.setDifficultyLevel(activeQuest.getQuestTemplate().getDifficultyLevel());
        dto.setEndTime(activeQuest.getEndTime());
        dto.setDurationMinutes(activeQuest.getQuestTemplate().getDurationMinutes());
        dto.setX(activeQuest.getMapSquare().getXCoord());
        dto.setY(activeQuest.getMapSquare().getYCoord());
        long remainingSeconds = java.time.Duration.between(now, activeQuest.getEndTime()).getSeconds();
        dto.setRemainingSeconds(Math.max(0, remainingSeconds));
        return dto;
    }

    // --- INTERNAL GAME LOGIC ---

    private boolean calculateSuccess(ActiveQuest activeQuest) {
        int beastieStat = activeQuest.getBeastieStatValue();
        int requiredStat = activeQuest.getRequiredStatValue();

        double successChance = 0.50;

        double statModifier = (beastieStat - requiredStat) * 0.10;

        successChance = Math.min(0.95, successChance + statModifier);
        successChance = Math.max(0.05, successChance);

        return random.nextDouble() < successChance;
    }

    private String handleSuccess(ActiveQuest activeQuest) {
        Beastie beastie = activeQuest.getBeastie();
        Quest quest = activeQuest.getQuestTemplate();

        int expGained = random.nextInt(quest.getMaxExpReward() - quest.getMinExpReward() + 1) + quest.getMinExpReward();

        beastie.setExperience(beastie.getExperience() + expGained);

        String candyReward = "";
        if (random.nextDouble() < 0.5) {
            CandyType[] types = CandyType.values();
            CandyType randomCandy = types[random.nextInt(types.length)];

            itemService.addCandy(beastie.getOwner().getId(), randomCandy, 1);
            candyReward = " y 1 " + randomCandy.getName();
        }

        beastieRepository.save(beastie);

        boolean leveledUp = levelUpService.checkForLevelUp(beastie.getId());

        String levelUpMessage = leveledUp
                ? String.format(" ¡%s level up to %d and stats upgraded!",
                beastie.getName(), beastie.getLevel())
                : "";

        return String.format("¡SUCCESS! %s completed the quest and earned %d EXP%s.%s",
                beastie.getName(), expGained, candyReward, levelUpMessage);
    }

    private String handleFailure(ActiveQuest activeQuest) {
        Beastie beastie = activeQuest.getBeastie();

        beastie.setSad(true);

        beastieRepository.save(beastie);
        return String.format("FAILURE. %s failed and is sad. She won't be able to participate in the next Quest..", beastie.getName());
    }

    private BeastieType getBeastieTypeForQuest(QuestType type) {
        return switch (type) {
            case EXPLORATION -> BeastieType.EXPLORER;
            case COMBAT -> BeastieType.FIGHTER;
            case PUZZLE -> BeastieType.SAGE;
        };
    }

    private int getBeastieStatValue(Beastie beastie, Stat stat) {
        return switch (stat) {
            case STRENGTH -> beastie.getStrength();
            case DEXTERITY -> beastie.getDexterity();
            case INTELLIGENCE -> beastie.getIntelligence();
        };
    }
}

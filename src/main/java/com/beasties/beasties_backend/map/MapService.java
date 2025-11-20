package com.beasties.beasties_backend.map;

import com.beasties.beasties_backend.exception.ResourceNotFoundException;
import com.beasties.beasties_backend.map.dto.MapSquareDTO;
import com.beasties.beasties_backend.quest.Quest;
import com.beasties.beasties_backend.quest.QuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MapService {

    private final MapSquareRepository mapSquareRepository;
    private final QuestRepository questRepository;
    private final Random random = new Random();

    public MapService(MapSquareRepository mapSquareRepository, QuestRepository questRepository) {
        this.mapSquareRepository = mapSquareRepository;
        this.questRepository = questRepository;
    }

    /**
     * [ADMIN] Initialize or regenerate the 5x5 map with random quests.
     */
    public List<MapSquareDTO> randomizeMap() {
        List<Quest> availableQuests = questRepository.findAll();
        List<MapSquare> allSquares = mapSquareRepository.findAll();

        if (availableQuests.isEmpty()) {
            throw new IllegalStateException("There are no quests available to assign. Create quests first.");
        }

        if (allSquares.isEmpty()) {
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    allSquares.add(new MapSquare(x, y));
                }
            }
        }

        for (MapSquare square : allSquares) {
            if (random.nextDouble() < 0.6) {
                int randomIndex = random.nextInt(availableQuests.size());
                square.setQuest(availableQuests.get(randomIndex));
            } else {
                square.setQuest(null);
            }
        }

        mapSquareRepository.saveAll(allSquares);
        return mapSquaresToDTO(allSquares);
    }

    /**
     * [USER/ADMIN] Gets the current state of the map.
     */
    public List<MapSquareDTO> getCurrentMap() {
        return mapSquaresToDTO(mapSquareRepository.findAll());
    }

    /**
     * [ADMIN] Remove the Quest from a specific box.
     */
    public MapSquareDTO clearSquare(int x, int y) {
        MapSquare square = mapSquareRepository.findByXCoordAndYCoord(x, y)
                .orElseThrow(() -> new ResourceNotFoundException("MapSquare", "coordenades", x + "," + y));

        square.setQuest(null);
        mapSquareRepository.save(square);
        return mapSquareToDTO(square);
    }

    private List<MapSquareDTO> mapSquaresToDTO(List<MapSquare> squares) {
        return squares.stream()
                .map(this::mapSquareToDTO)
                .collect(Collectors.toList());
    }

    private MapSquareDTO mapSquareToDTO(MapSquare square) {
        Quest quest = square.getQuest();
        return MapSquareDTO.builder()
                .xCoord(square.getXCoord())
                .yCoord(square.getYCoord())
                .questId(quest != null ? quest.getId() : null)
                .questName(quest != null ? quest.getName() : "Empty")
                .questDifficulty(quest != null ? quest.getDifficultyLevel() : 0)
                .build();
    }
}

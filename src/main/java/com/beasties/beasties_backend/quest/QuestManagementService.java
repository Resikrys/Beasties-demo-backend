package com.beasties.beasties_backend.quest;

import com.beasties.beasties_backend.exception.ResourceNotFoundException;
import com.beasties.beasties_backend.quest.dto.QuestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestManagementService {

    private final QuestRepository questRepository;
    private final QuestMapper questMapper;

    public QuestManagementService(QuestRepository questRepository, QuestMapper questMapper) {
        this.questRepository = questRepository;
        this.questMapper = questMapper;
    }

    public QuestDTO createQuest(QuestDTO dto) {
        Quest newQuest = questMapper.toEntity(dto);
        Quest savedQuest = questRepository.save(newQuest);
        return questMapper.toDTO(savedQuest);
    }

    public List<QuestDTO> getAllQuests() {
        return questMapper.toDTOList(questRepository.findAll());
    }

    public QuestDTO getQuestById(Long id) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest", "id", id));
        return questMapper.toDTO(quest);
    }

    public QuestDTO updateQuest(Long id, QuestDTO dto) {
        Quest existingQuest = questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest", "id", id));

        Quest updatedQuest = questMapper.toEntity(dto);
        updatedQuest.setId(existingQuest.getId());

        if (updatedQuest.getMinExpReward() > updatedQuest.getMaxExpReward()) {
            throw new IllegalArgumentException("The minimum reward cannot be greater than the maximum..");
        }

        Quest savedQuest = questRepository.save(updatedQuest);
        return questMapper.toDTO(savedQuest);
    }

    public void deleteQuest(Long id) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest", "id", id));

        questRepository.delete(quest);
    }
}

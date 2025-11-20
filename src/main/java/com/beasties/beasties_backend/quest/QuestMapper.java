package com.beasties.beasties_backend.quest;

import com.beasties.beasties_backend.quest.dto.QuestDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestMapper {

    public QuestDTO toDTO(Quest quest) {
        QuestDTO dto = new QuestDTO();
        dto.setId(quest.getId());
        dto.setName(quest.getName());
        dto.setDescription(quest.getDescription());
        dto.setType(quest.getType());
        dto.setDifficultyLevel(quest.getDifficultyLevel());
        dto.setRequiredStat(quest.getRequiredStat());
        dto.setBaseStatRequirement(quest.getBaseStatRequirement());
        dto.setMinExpReward(quest.getMinExpReward());
        dto.setMaxExpReward(quest.getMaxExpReward());
        dto.setDurationMinutes(quest.getDurationMinutes());
        return dto;
    }

    public Quest toEntity(QuestDTO dto) {
        Quest quest = new Quest();
        quest.setName(dto.getName());
        quest.setDescription(dto.getDescription());
        quest.setType(dto.getType());
        quest.setDifficultyLevel(dto.getDifficultyLevel());
        quest.setRequiredStat(dto.getRequiredStat());
        quest.setBaseStatRequirement(dto.getBaseStatRequirement());
        quest.setMinExpReward(dto.getMinExpReward());
        quest.setMaxExpReward(dto.getMaxExpReward());
        quest.setDurationMinutes(dto.getDurationMinutes());
        return quest;
    }

    public List<QuestDTO> toDTOList(List<Quest> quests) {
        return quests.stream().map(this::toDTO).collect(Collectors.toList());
    }
}

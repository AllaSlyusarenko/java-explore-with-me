package ru.practicum.ewm.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.request.dto.ParticipationResponseDto;
import ru.practicum.ewm.request.model.Participation;

@UtilityClass
public class ParticipationMapper {
    public ParticipationResponseDto toParticipationResponseDto(Participation participation) {
        ParticipationResponseDto participationResponseDto = new ParticipationResponseDto();
        participationResponseDto.setId(participation.getId());
        participationResponseDto.setCreated(participation.getCreated());
        participationResponseDto.setEvent(participation.getEvent().getId());
        participationResponseDto.setRequester(participation.getRequester().getId());
        participationResponseDto.setStatus(participation.getStatus());
        return participationResponseDto;
    }
}
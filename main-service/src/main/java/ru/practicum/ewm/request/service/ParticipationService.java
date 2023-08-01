package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationResponseDto;

import java.util.List;

public interface ParticipationService {
    ParticipationResponseDto saveRequest(Long userId, Long eventId);

    List<ParticipationResponseDto> getRequestByOtherUserEvents(Long userId);

    ParticipationResponseDto cancelRequestByUser(Long userId, Long requestId);
}
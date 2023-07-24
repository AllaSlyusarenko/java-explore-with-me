package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.repository.EventFilter;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    public EventFullDto getEventByUserIdEventId(Long userId, Long eventId);

    public EventFullDto updateEventByUserIdEventId(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    public List<EventFullDto> getAllEventsAdmin(EventFilter filter);

    public ParticipationResponseDto saveRequest(Long userId, Long eventId);

    public List<ParticipationResponseDto> getRequestByOtherUserEvents(Long userId);

    public ParticipationResponseDto cancelRequestByUser(Long userId, Long requestId);

    public List<ParticipationResponseDto> getRequestsByUserEvent(Long userId, Long eventId);

    public EventRequestStatusUpdateResult updateRequestStatusByUserEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}

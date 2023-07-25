package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.repository.EventFilter;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    public EventFullDto getEventByUserIdEventId(Long userId, Long eventId);

    public EventFullDto updateEventByUserIdEventId(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    public List<EventFullDto> getAllEventsAdmin(List<Long> users, EventState states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    public ParticipationResponseDto saveRequest(Long userId, Long eventId);

    public List<ParticipationResponseDto> getRequestByOtherUserEvents(Long userId);

    public ParticipationResponseDto cancelRequestByUser(Long userId, Long requestId);

    public List<ParticipationResponseDto> getRequestsByUserEvent(Long userId, Long eventId);

    public EventRequestStatusUpdateResult updateRequestStatusByUserEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    public List<EventShortDto> getEventsWithFilter(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    public EventFullDto getEventById(Long id, HttpServletRequest request);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}

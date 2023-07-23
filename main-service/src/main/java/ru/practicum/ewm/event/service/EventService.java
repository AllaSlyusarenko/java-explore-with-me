package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    public EventFullDto getEventByUserIdEventId(Long userId, Long eventId);

    public EventFullDto updateEventByUserIdEventId(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    public List<EventFullDto> getAllEventsAdmin(List<Long> users, EventState states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}

package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;

import java.util.List;

public interface EventService {
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);
}

package ru.practicum.ewm.event.repository;

import lombok.*;
import ru.practicum.ewm.event.dto.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class EventFilter {
    private List<Long> users;
    private List<EventState> states;
    //private final String text;
    private List<Long> categories;
    //private final Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    //private final boolean onlyAvailable;
}
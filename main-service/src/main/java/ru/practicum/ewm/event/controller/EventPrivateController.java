package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable(value = "userId") Long userId,
                                  @Valid @RequestBody NewEventDto newEventDto) {
        log.debug("Создание события: {}", newEventDto);
        return eventService.saveEvent(userId, newEventDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUserId(@PathVariable(value = "userId") Long userId,
                                                 @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Получение событий для: {}", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByUserIdEventId(@PathVariable(value = "userId") Long userId,
                                                @PathVariable(value = "eventId") Long eventId) {
        log.debug("Получение события для пользователя  {} с eventId {}", userId, eventId);
        return eventService.getEventByUserIdEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByUserIdEventId(@PathVariable(value = "userId") Long userId,
                                                   @PathVariable(value = "eventId") Long eventId,
                                                   @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.debug("Изменение события пользователем {} для eventId {}", userId, eventId);
        return eventService.updateEventByUserIdEventId(userId, eventId,updateEventUserRequest);
    }
}
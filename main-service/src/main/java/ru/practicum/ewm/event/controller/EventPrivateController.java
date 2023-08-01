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
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationResponseDto;

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

    @PostMapping //добавление нового события
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable(name = "userId") Long userId,
                                  @Valid @RequestBody NewEventDto newEventDto) {
        log.debug("Создание события: {}", newEventDto);
        return eventService.saveEvent(userId, newEventDto);
    }

    @GetMapping // события, добавленные текущим пользователем
    public List<EventShortDto> getEventsByUserId(@PathVariable(name = "userId") Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Получение событий для: {}", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}") // полная инфа события, добавленного текущим пользователем
    public EventFullDto getEventByUserIdEventId(@PathVariable(name = "userId") Long userId,
                                                @PathVariable(name = "eventId") Long eventId) {
        log.debug("Получение события для пользователя  {} с eventId {}", userId, eventId);
        return eventService.getEventByUserIdEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}") //изменение события, добавленного текущим пользователем
    public EventFullDto updateEventByUserIdEventId(@PathVariable(name = "userId") Long userId,
                                                   @PathVariable(name = "eventId") Long eventId,
                                                   @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.debug("Изменение события пользователем {} для eventId {}", userId, eventId);
        return eventService.updateEventByUserIdEventId(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests") // инфа о запросах на участие в событии текущего пользователя
    public List<ParticipationResponseDto> getRequestsByUserEvent(@PathVariable(name = "userId") @Positive Long userId,
                                                                 @PathVariable(name = "eventId") @Positive Long eventId) {
        log.debug("Получение запросов на событие пользователя  {} с eventId {}", userId, eventId);
        return eventService.getRequestsByUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    // изм-е статуса(подтверждена, отменена) заявок на участие тек пользователя
    public EventRequestStatusUpdateResult updateRequestStatusByUserEvent(@PathVariable(name = "userId") @Positive Long userId,
                                                                         @PathVariable(name = "eventId") @Positive Long eventId,
                                                                         @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.debug("Изменение статуса запросов на событие пользователя  {} с eventId {}", userId, eventId);
        return eventService.updateRequestStatusByUserEvent(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventAdminController {
    private final EventService eventService;

    @GetMapping // поиск событий
    public List<EventFullDto> getAllEventsAdmin(@RequestParam(name = "users", required = false) List<Long> users,
                                                @RequestParam(name = "states", required = false) EventState states,
                                                @RequestParam(name = "categories", required = false) List<Long> categories,
                                                @RequestParam(name = "rangeStart", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(name = "rangeEnd", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Получение событий для: {}, {},{},{},{}", users, states, categories, rangeStart, rangeEnd);
        return eventService.getAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}") // редактирование данных события и его статуса(отклонение/публикация)
    public EventFullDto updateEventAdmin(@PathVariable(name = "eventId") Long eventId,
                                         @Validated @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("Изменение события администратором для: {}", eventId);
        return eventService.updateEventAdmin(eventId, updateEventAdminRequest);
    }
}
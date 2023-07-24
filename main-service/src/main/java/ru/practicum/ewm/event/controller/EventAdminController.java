package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.repository.EventFilter;
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

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventFullDto> getAllEventsAdmin(@RequestParam(name = "users", required = false) List<Long> users,
//                                                @RequestParam(name = "states", required = false) List<EventState> states,
//                                                @RequestParam(name = "categories", required = false) List<Long> categories,
//                                                @RequestParam(name = "rangeStart", required = false)
//                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
//                                                @RequestParam(name = "rangeEnd", required = false)
//                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
//                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
//                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
//        log.debug("Получение событий для: {}, {},{},{},{}", users, states, categories, rangeStart, rangeEnd);
//        EventFilter filter = new EventFilter(users, states, categories, rangeStart, rangeEnd, from, size);
//        return eventService.getAllEventsAdmin(filter);
//    }

//    @PatchMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventFullDto> updateEventAdmin(@RequestParam(required = false) List<Long> users,
//                                                @RequestParam(required = false) EventState states,
//                                                @RequestParam(required = false) List<Long> categories,
//                                                @RequestParam(required = false)
//                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
//                                                @RequestParam(required = false)
//                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
//                                                @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
//                                                @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
//        log.debug("Получение событий для: {}, {},{},{},{}", users, states, categories, rangeStart, rangeEnd);
//        return eventService.updateEventAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
//    }
}

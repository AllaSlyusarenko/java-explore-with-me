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
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventPublicController {
    private final EventService eventService;

    @GetMapping() // получение событий с возможностью фильтрации
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsWithFilter(@RequestParam(name = "text", required = false) String text,
                                                   @RequestParam(name = "categories", required = false) List<Long> categories,
                                                   @RequestParam(name = "paid", required = false) Boolean paid,
                                                   @RequestParam(name = "rangeStart", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam(name = "rangeEnd", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                   @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                                   @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
                                                   @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                                   HttpServletRequest request) {
        log.debug("Получение событий с возможностью фильтрации: {},{},{},{},{},{},{}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        return eventService.getEventsWithFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable(name = "id") Long id,
                                     HttpServletRequest httpServletRequest) {
        log.debug("Получение события по id: {}", id);
        return eventService.getEventById(id, httpServletRequest);
    }
}
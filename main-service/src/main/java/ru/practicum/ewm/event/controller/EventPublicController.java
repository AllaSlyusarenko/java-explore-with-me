package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpRequest;
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
    public List<EventShortDto> getEventsWithFilter(@RequestParam(value = "text") String text,
                                                   @RequestParam(value = "categories") List<Long> categories,
                                                   @RequestParam(value = "paid") Boolean paid,
                                                   @RequestParam(value = "rangeStart")
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam(value = "rangeEnd")
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                   @RequestParam(value = "onlyAvailable") Boolean onlyAvailable,
                                                   @RequestParam(value = "sort", defaultValue = "EVENT_DATE") String sort,
                                                   @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") @Positive Integer size,
                                                   HttpServletRequest request) {
        log.debug("Получение событий с возможностью фильтрации: {},{},{},{},{},{},{}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        return eventService.getEventsWithFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable(value = "id") Long id,
                                     HttpServletRequest request) {
        log.debug("Получение события по id: {}", id);
        return eventService.getEventById(id, request);
    }
}

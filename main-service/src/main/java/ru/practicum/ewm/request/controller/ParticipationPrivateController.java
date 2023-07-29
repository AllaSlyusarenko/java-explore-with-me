package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationResponseDto;
import ru.practicum.ewm.request.service.ParticipationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParticipationPrivateController {
    private final ParticipationService participationService;

    @PostMapping // добавление запроса от текущего пользователя
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationResponseDto saveRequest(@PathVariable(name = "userId") @Positive Long userId,
                                                @RequestParam(name = "eventId") @Positive Long eventId) {
        log.debug("Создание запроса на событие пользователя  {} с eventId {}", userId, eventId);
        return participationService.saveRequest(userId, eventId);
    }

    @GetMapping // инфа о заявках текущего пользователя в чужих событиях
    public List<ParticipationResponseDto> getRequestByOtherUserEvents(@PathVariable(name = "userId") @Positive Long userId) {
        log.debug("Запросы пользователя с id {} на участие в событиях", userId);
        return participationService.getRequestByOtherUserEvents(userId);
    }

    @PatchMapping("/{requestId}/cancel") // отмена своего запроса на участие
    public ParticipationResponseDto cancelRequestByUser(@PathVariable(value = "userId") Long userId,
                                                        @PathVariable(value = "requestId") @Positive Long requestId) {
        log.debug("Отмена пользователем с id {} на участие в событии запроса с id {}", userId, requestId);
        return participationService.cancelRequestByUser(userId, requestId);
    }
}
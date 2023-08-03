package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentResponseDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentPublicController {
    private final CommentService commentService;


    @GetMapping //все комменты по дате
    public List<CommentResponseDto> getAllComments(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Просмотр всех комментариев");
        return commentService.getAllComments(from, size);
    }

    @GetMapping("/{eventId}") //все комменты публичного ивента
    public List<CommentResponseDto> getAllCommentsByEvent(@PathVariable(name = "eventId") Long eventId,
                                                          @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                          @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Просмотр всех комментариев события {}", eventId);
        return commentService.getAllCommentsByEvent(eventId, from, size);
    }
}
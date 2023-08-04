package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentResponseDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/private/comments/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/{eventId}") //добавление нового комментария, статус PENDING
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto saveComment(@PathVariable(name = "userId") Long userId,
                                          @PathVariable(name = "eventId") Long eventId,
                                          @Valid @RequestBody NewCommentDto newCommentDto) {
        log.debug("Создание комментария к событию: {}", eventId);
        return commentService.saveComment(userId, eventId, newCommentDto);
    }

    @GetMapping //все комментарии пользователя со статусами  PENDING, CANCELED, PUBLISHED
    public List<CommentResponseDto> getAllUserComments(@PathVariable(name = "userId") Long userId,
                                                       @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Просмотр всех комментариев пользователя {}", userId);
        return commentService.getAllUserComments(userId, from, size);
    }

    @DeleteMapping("/{commId}")
    // комментарий не удаляется, но переходит в статус CANCELED, при котором комментарий виден только его автору, изменить комментарий уже нельзя
    public void deleteCommentById(@PathVariable(name = "userId") Long userId,
                                  @PathVariable(name = "commId") Long commId) {
        log.debug("Удаление видимости для комментария {}", commId);
        commentService.deleteCommentById(userId, commId);
    }

    @PatchMapping("/{commId}")
    // после изменения статус снова PENDING, менять можно комментарии со статусами PENDING, PUBLISHED
    public CommentResponseDto updateCommentById(@PathVariable(name = "userId") Long userId,
                                                @PathVariable(name = "commId") Long commId,
                                                @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.debug("Изменение комментария с id: {}", commId);
        return commentService.updateCommentById(userId, commId, updateCommentDto);
    }

    @GetMapping("/{commId}") //найдется любой, кроме того, у которого статус REJECTED
    public CommentResponseDto getCommentById(@PathVariable(name = "userId") Long userId,
                                             @PathVariable(name = "commId") Long commId) {
        log.debug("Просмотр комментария по id {}", commId);
        return commentService.getCommentById(userId, commId);
    }
}
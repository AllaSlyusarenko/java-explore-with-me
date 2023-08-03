package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentResponseDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentAdminController {
    private final CommentService commentService;

    @PatchMapping("/{commId}") // админ публикует или отклоняет комментарий, статус должен быть PENDING
    public CommentResponseDto changeStatus(@PathVariable(name = "commId") Long commId,
                                           @RequestParam(name = "status") @NotBlank String status) {
        log.debug("Изменение статуса комментария {}", commId);
        return commentService.changeStatus(commId, status);
    }

    @DeleteMapping("/{userId}")
    // если юзер забанен, админ отклоняет все комментарии, не удаляет, а переводит в статус REJECTED
    public List<CommentResponseDto> deleteAllUserComment(@PathVariable(name = "userId") Long userId) {
        log.debug("Удаление всех комментариев пользователя {}", userId);
        return commentService.deleteAllUserComment(userId);
    }

    @DeleteMapping("/{commId}") // админ отклоняет комментарий с любым статусом
    public CommentResponseDto deleteCommentByIdAdmin(@PathVariable(name = "commId") Long commId) {
        log.debug("Изменение статуса комментария {}", commId);
        return commentService.deleteCommentByIdAdmin(commId);
    }
}
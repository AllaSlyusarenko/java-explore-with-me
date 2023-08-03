package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.service.LikeService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/likes", produces = MediaType.APPLICATION_JSON_VALUE)
public class LikePublicController {
    private final LikeService likeService;

    @PostMapping("/{userId}/{commId}/like") //like комментарию от любого пользователя
    public void saveLike(@PathVariable(name = "userId") Long userId,
                         @PathVariable(name = "commId") Long commId) {
        log.debug("Сохранение like к комментарию {}", commId);
        likeService.saveLike(userId, commId);
    }

    @PostMapping("/{userId}/{commId}/dislike") //dislike комментарию от любого пользователя
    public void saveDislike(@PathVariable(name = "userId") Long userId,
                            @PathVariable(name = "commId") Long commId) {
        log.debug("Сохранение dislike к комментарию {}", commId);
        likeService.saveDislike(userId, commId);
    }
}
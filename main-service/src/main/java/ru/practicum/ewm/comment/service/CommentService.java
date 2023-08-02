package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentResponseDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto saveComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    List<CommentResponseDto> getAllUserComments(Long userId, Integer from, Integer size);

    void deleteCommentById(Long userId, Long commId);

    CommentResponseDto updateCommentById(Long userId, Long commId, UpdateCommentDto updateCommentDto);

    CommentResponseDto getCommentById(Long userId, Long commId);

    CommentResponseDto changeStatus(Long commId, String status);

    void deleteAllUserComment(Long userId);
}

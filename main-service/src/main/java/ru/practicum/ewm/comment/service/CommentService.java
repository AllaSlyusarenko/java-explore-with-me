package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto saveComment(Long userId, Long eventId, CommentDto newCommentDto);

    List<CommentResponseDto> getAllUserComments(Long userId, Integer from, Integer size);

    void deleteCommentById(Long userId, Long commId);

    CommentResponseDto updateCommentById(Long userId, Long commId, CommentDto updateCommentDto);

    CommentResponseDto getCommentById(Long userId, Long commId);

    CommentResponseDto changeStatus(Long commId, String status);

    List<CommentResponseDto> deleteAllUserComment(Long userId);

    CommentResponseDto deleteCommentByIdAdmin(Long commId);

    List<CommentResponseDto> getAllComments(Integer from, Integer size);

    List<CommentResponseDto> getAllCommentsByEvent(Long eventId, Integer from, Integer size);

    CommentResponseDto getPublicCommentById(Long commId);
}
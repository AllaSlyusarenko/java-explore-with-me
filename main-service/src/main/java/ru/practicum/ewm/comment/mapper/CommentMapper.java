package ru.practicum.ewm.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.dto.CommentResponseDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public Comment newCommentDtoToComment(User user, Event event, LocalDateTime created, NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(created);
        comment.setStatus(CommentStatus.PENDING);
        return comment;
    }

    public CommentResponseDto commentToCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setEvent(EventMapper.eventToEventShortDto(comment.getEvent()));
        commentResponseDto.setAuthor(UserMapper.userToUserShortDto(comment.getAuthor()));
        commentResponseDto.setCreated(comment.getCreated());
        commentResponseDto.setStatus(comment.getStatus().name());
        commentResponseDto.setEditTime(comment.getEditTime() == null ? comment.getEditTime() : null);
        return commentResponseDto;
    }

    public List<CommentResponseDto> commentsToDtos(List<Comment> comments) {
        return comments.stream().map(CommentMapper::commentToCommentResponseDto).collect(Collectors.toList());
    }
}

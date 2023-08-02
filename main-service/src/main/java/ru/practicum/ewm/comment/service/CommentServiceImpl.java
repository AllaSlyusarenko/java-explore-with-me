package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentResponseDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto saveComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        LocalDateTime created = LocalDateTime.now();
        Comment comment = CommentMapper.newCommentDtoToComment(user, event, created, newCommentDto);
        Comment commentSave = commentRepository.save(comment);
        return CommentMapper.commentToCommentResponseDto(commentSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllUserComments(Long userId, Integer from, Integer size) { //все комменты пользователя
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));
        List<Comment> comments = commentRepository.findAllByAuthor_Id(userId, pageable);
        return CommentMapper.commentsToDtos(comments);
    }

    @Override
    public void deleteCommentById(Long userId, Long commId) { // из базы не удаляется, меняется статус
        Comment comment = commentRepository.findByIdAndAuthor_Id(commId, userId);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден");
        }
        comment.setStatus(CommentStatus.CANCELED);// статус - отменен, изменению не подлежит
        commentRepository.save(comment);
        //удалить также лайки этого комментария
    }

    @Override
    public CommentResponseDto updateCommentById(Long userId, Long commId, UpdateCommentDto updateCommentDto) {
        // изменение комментария, статус опять "в ожидании" модерации админом
        Comment comment = commentRepository.findByIdAndAuthor_Id(commId, userId);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден");
        }
        comment.setText(updateCommentDto.getText());
        comment.setStatus(CommentStatus.PENDING);
        comment.setEditTime(LocalDateTime.now());
        Comment commentSave = commentRepository.save(comment);
        return CommentMapper.commentToCommentResponseDto(commentSave);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long userId, Long commId) { //свой коммент по id
        Comment comment = commentRepository.findByIdAndAuthor_Id(commId, userId);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден");
        }
        return CommentMapper.commentToCommentResponseDto(comment);
    }

    @Override
    public CommentResponseDto changeStatus(Long commId, String status) {
        Comment comment = commentRepository.findById(commId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        if (comment.getStatus() != CommentStatus.PENDING) {
            throw new ConflictException("Одобрен/отклонен может быть только комментарий в состоянии ожидания");
        }
        if (status.equals("PUBLISHED")) {
            comment.setStatus(CommentStatus.PUBLISHED);
        } else if (status.equals("REJECTED")) {
            comment.setStatus(CommentStatus.REJECTED);
        } else {
            throw new ConflictException("Неизвестный статус");
        }
        Comment commentSave = commentRepository.save(comment);
        return CommentMapper.commentToCommentResponseDto(commentSave);
    }

    @Override
    public void deleteAllUserComment(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<Comment> comments = commentRepository.findAllByAuthor_Id(userId);
        comments.forEach(comment -> comment.setStatus(CommentStatus.CANCELED));
        commentRepository.saveAll(comments);
    }
}
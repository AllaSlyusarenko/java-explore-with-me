package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentResponseDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto saveComment(Long userId, Long eventId, CommentDto newCommentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Событие не найдено");
        }
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
        List<Comment> comments = commentRepository.findAllByAuthorAndStatusNot(user, CommentStatus.REJECTED, pageable);
        return CommentMapper.commentsToDtos(comments);
    }

    @Override
    public void deleteCommentById(Long userId, Long commId) { // из базы не удаляется, меняется статус
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Comment comment = commentRepository.findByIdAndAuthor(commId, user);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден");
        }
        comment.setStatus(CommentStatus.CANCELED);// статус - отменен, изменению не подлежит
        commentRepository.save(comment);
    }

    @Override
    public CommentResponseDto updateCommentById(Long userId, Long commId, CommentDto updateCommentDto) {
        // изменение комментария, статус опять "в ожидании" модерации админом
        //менять можно комментарии со статусами PENDING, PUBLISHED
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<CommentStatus> statuses = List.of(CommentStatus.PENDING, CommentStatus.PUBLISHED);
        Comment comment = commentRepository.findByIdAndAuthorAndStatusIn(commId, user, statuses);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден или его невозможно изменить");
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Comment comment = commentRepository.findByIdAndAuthorAndStatusNot(commId, user, CommentStatus.REJECTED);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден");
        }
        return CommentMapper.commentToCommentResponseDto(comment);
    }

    @Override
    public CommentResponseDto changeStatus(Long commId, String status) { // публикация/отклонен
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
    public List<CommentResponseDto> deleteAllUserComment(Long userId) { // удаление/скрытие всех комментариев пользователя
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<Comment> comments = commentRepository.findAllByAuthorAndStatusNot(user, CommentStatus.REJECTED);
        comments.forEach(comment -> comment.setStatus(CommentStatus.REJECTED));
        List<Comment> commentsSave = commentRepository.saveAll(comments);
        return CommentMapper.commentsToDtos(commentsSave);
    }

    @Override
    public CommentResponseDto deleteCommentByIdAdmin(Long commId) { // удаление/скрытие комментария с любым статусом
        Comment comment = commentRepository.findById(commId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        comment.setStatus(CommentStatus.REJECTED);
        Comment commentSave = commentRepository.save(comment);
        return CommentMapper.commentToCommentResponseDto(commentSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllComments(Integer from, Integer size) { // только опубликованные комментарии
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));
        List<Comment> comments = commentRepository.findAllByStatus(CommentStatus.PUBLISHED, pageable);
        return CommentMapper.commentsToDtos(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllCommentsByEvent(Long eventId, Integer from, Integer size) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Комментарии можно оставлять только опубликованным событиям");
        }
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));
        List<Comment> comments = commentRepository.findAllByEventAndStatus(event, CommentStatus.PUBLISHED, pageable);
        return CommentMapper.commentsToDtos(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto getPublicCommentById(Long commId) {
        Comment comment = commentRepository.findByIdAndStatus(commId, CommentStatus.PUBLISHED);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден");
        }
        return CommentMapper.commentToCommentResponseDto(comment);
    }
}
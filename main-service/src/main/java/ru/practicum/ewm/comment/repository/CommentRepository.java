package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndAuthor_Id(Long commId, Long userId);

    Comment findByIdAndAuthor_IdAndStatusNot(Long commId, Long userId, CommentStatus status);

    List<Comment> findAllByAuthor_IdAndStatusNot(Long userId, CommentStatus status, Pageable pageable);

    List<Comment> findAllByAuthor_IdAndStatusNot(Long userId, CommentStatus status);

    List<Comment> findAllByEvent_Id(Long eventId, Pageable pageable);

    Comment findByIdAndAuthor_IdAndStatusIn(Long commId, Long userId, List<CommentStatus> statuses);
}
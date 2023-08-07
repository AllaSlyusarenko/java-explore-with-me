package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndAuthor(Long commId, User user);

    Comment findByIdAndAuthorAndStatusNot(Long commId, User user, CommentStatus status);

    List<Comment> findAllByAuthorAndStatusNot(User user, CommentStatus status, Pageable pageable);

    List<Comment> findAllByAuthorAndStatusNot(User user, CommentStatus status);

    Comment findByIdAndAuthorAndStatusIn(Long commId, User user, List<CommentStatus> statuses);

    List<Comment> findAllByStatus(CommentStatus status, Pageable pageable);

    List<Comment> findAllByEventAndStatus(Event event, CommentStatus status, Pageable pageable);

    Comment findByIdAndStatus(Long commId, CommentStatus status);
}
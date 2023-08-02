package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthor_Id(Long userId, Pageable pageable);

    Comment findByIdAndAuthor_Id(Long commId, Long userId);

    List<Comment> findAllByAuthor_Id(Long userId);

}
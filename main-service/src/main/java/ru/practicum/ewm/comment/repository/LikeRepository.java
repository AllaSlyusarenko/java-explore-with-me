package ru.practicum.ewm.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.comment.model.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByLiker_IdAndComment_IdAndPositive(Long userId, Long commId, boolean isLike);
}
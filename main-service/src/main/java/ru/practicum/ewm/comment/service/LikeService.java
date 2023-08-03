package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.Like;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.repository.LikeRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public void saveLike(Long userId, Long commId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Comment comment = commentRepository.findById(commId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        Like likeInDB = likeRepository.findByLiker_IdAndComment_IdAndPositive(userId, commId, true);
        if (likeInDB != null) {
            likeRepository.delete(likeInDB);
        }
        Like dislikeInDB = likeRepository.findByLiker_IdAndComment_IdAndPositive(userId, commId, false);
        if (dislikeInDB != null) {
            likeRepository.delete(dislikeInDB);
            dislikeInDB.setPositive(true);
            likeRepository.save(dislikeInDB);
        }
        if (likeInDB == null && dislikeInDB == null) {
            Like like = new Like().toBuilder().comment(comment).positive(true).liker(user).created(LocalDateTime.now()).build();
            likeRepository.save(like);
        }
    }

    public void saveDislike(Long userId, Long commId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Comment comment = commentRepository.findById(commId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        Like dislikeInDB = likeRepository.findByLiker_IdAndComment_IdAndPositive(userId, commId, false);
        if (dislikeInDB != null) {
            likeRepository.delete(dislikeInDB);
        }
        Like likeInDB = likeRepository.findByLiker_IdAndComment_IdAndPositive(userId, commId, true);
        if (likeInDB != null) {
            likeRepository.delete(likeInDB);
            likeInDB.setPositive(false);
            likeRepository.save(likeInDB);
        }
        if (likeInDB == null && dislikeInDB == null) {
            Like dislike = new Like().toBuilder().comment(comment).positive(false).liker(user).created(LocalDateTime.now()).build();
            likeRepository.save(dislike);
        }
    }
}
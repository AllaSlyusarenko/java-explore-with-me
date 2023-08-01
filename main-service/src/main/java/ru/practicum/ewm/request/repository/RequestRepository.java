package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.dto.ParticipationStatus;
import ru.practicum.ewm.request.model.Participation;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByRequester(User requester);

    List<Participation> findAllByEvent_Id(Long eventId);

    Participation findAllByRequester_IdAndEvent_Id(Long userId, Long eventId);

    Participation findByIdAndRequester_Id(Long requestId, Long userId);

    List<Participation> findAllByEvent_IdAndIdInAndStatus(Long eventId, Set<Long> ids, ParticipationStatus status);
}
package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationStatus;
import ru.practicum.ewm.request.model.Participation;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByRequester(User requester);

    List<Participation> findAllByEventAndStatusOrderByCreated(Event event, ParticipationStatus status);

    List<Participation> findAllByEvent_Id(Long eventId);

    Participation findFirstByEvent_IdAndRequester_Id(Long eventId, Long userId);
    Participation findByIdAndRequester_Id(Long requestId, Long userId);
}
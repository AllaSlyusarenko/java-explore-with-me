package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.RequestStatus;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    public List<Request> findAllByRequester(User requester);

    List<Request> findAllByEventAndStatusOrderByCreated(Event event, RequestStatus status);

    List<Request> findAllByEvent(Event event);

    Request findFirstByEvent_IdAndRequester_Id(Long eventId, Long userId);
}

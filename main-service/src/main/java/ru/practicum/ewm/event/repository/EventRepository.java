package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.model.Event;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorOrderById(User user, Pageable pageable);

    Page<Event> findAll(Specification<Event> spec, Pageable pageable);

    Event findByIdAndInitiator_Id(Long eventId, Long userId);

    Event findByIdAndState(Long eventId, EventState state);

    List<Event> findAllByCategory_Id(Long categoryId);
}
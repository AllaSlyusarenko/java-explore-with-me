package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationResponseDto;
import ru.practicum.ewm.request.dto.ParticipationStatus;
import ru.practicum.ewm.request.mapper.ParticipationMapper;
import ru.practicum.ewm.request.model.Participation;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public ParticipationResponseDto saveRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (requestRepository.findAllByRequester_IdAndEvent_Id(userId, eventId) != null) {
            throw new ConflictException("Повторный запрос на участие в событии");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Мероприятие достигло лимита участников");
        }
        if (!(event.getState().equals(EventState.PUBLISHED))) {
            throw new ConflictException("Мероприятие пока не опубликовано");
        }
        if (user.equals(event.getInitiator())) {
            throw new ConflictException(("Инициатор и участник - одно лицо"));
        }
        ParticipationStatus status;
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
            status = ParticipationStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            status = ParticipationStatus.PENDING;
        }
        Participation participation = Participation.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();
        Participation participationSave = requestRepository.save(participation);
        return ParticipationMapper.toParticipationResponseDto(participationSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationResponseDto> getRequestByOtherUserEvents(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<Participation> participations = requestRepository.findAllByRequester(user);
        return participations.stream().map(ParticipationMapper::toParticipationResponseDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationResponseDto cancelRequestByUser(Long userId, Long requestId) {
        Participation participation = requestRepository.findByIdAndRequester_Id(requestId, userId);
        if (participation == null) {
            throw new NotFoundException("Событие не найдено");
        }
        Event event = participation.getEvent();
        if (participation.getStatus() == ParticipationStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            Event eventSave = eventRepository.save(event);
        }
        participation.setStatus(ParticipationStatus.CANCELED);
        Participation participationSave = requestRepository.save(participation);
        return ParticipationMapper.toParticipationResponseDto(participationSave);
    }
}
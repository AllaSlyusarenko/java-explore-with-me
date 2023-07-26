package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.client.stats.StatsClient;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.LocationMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationResponseDto;
import ru.practicum.ewm.request.dto.ParticipationStatus;
import ru.practicum.ewm.request.mapper.ParticipationMapper;
import ru.practicum.ewm.request.model.Participation;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utility.Constants;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;

//
//    public Map<Long, Long> getViews(List<Event> events) {
//
//        List<String> uris = events.stream()
//                .map(Event::getId)
//                .map(id -> "/events/" + id.toString())
//                .collect(Collectors.toUnmodifiableList());
//
//        List<ViewStatsResponse> eventStats = statsClient.getStats(uris);
//
//        Map<Long, Long> views = eventStats.stream()
//                .filter(statRecord -> statRecord.getApp().equals("ewm-service"))
//                .collect(Collectors.toMap(
//                                statRecord -> {
//                                    Pattern pattern = Pattern.compile("\\/events\\/([0-9]*)");
//                                    Matcher matcher = pattern.matcher(statRecord.getUri());
//                                    return Long.parseLong(matcher.group(1));
//                                },
//                                ViewStatsDto::getHits
//                        )
//                );
//        return views;
//    }

//    !!!public Map<Long, Long> getViews(List<Event> events) {
//        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
//        List<String> uris;
//        if (eventIds.isEmpty()) {
//            uris = List.of("/events/");
//        } else {
//            uris = eventIds.stream().map(event -> "/events/" + event).collect(Collectors.toList());
//        }
//
//        return toStatsMap(
//                statsClient.getStats(null, null, uris, null));
//    }
//
//    !!!public static Map<Long, Long> toStatsMap(List<ViewStatsResponse> stats) {
//        return stats.stream()
//                .collect(
//                        toMap(stat -> Long.valueOf(stat.getUri().substring(8)), ViewStatsResponse::getHits)
//                );
//    }

//    public Map<Long, Integer> findViews(Set<Event> events) {
//        return ViewsMapper.toStatsMap(
//                statsClient.getStat(events.stream().map(Event::getId).collect(Collectors.toList()))
//        );
//    }
//
//    public Map<Long, Integer> findViews(Long eventId) {
//        return ViewsMapper.toStatsMap(
//                statsClient.getStat(List.of(eventId))
//        );
//    }
//
//    public Map<Long, Integer> findViews(Compilation compilation) {
//        return ViewsMapper.toStatsMap(
//                statsClient.getStat(compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList()))
//        );
//    }


    @Override
    @Transactional
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        LocalDateTime created = LocalDateTime.now();
        if (LocalDateTime.parse(newEventDto.getEventDate(), Constants.formatterDate).isBefore(created.plusHours(2))) {
            throw new ValidationException("Указана неверная дата");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        Location location = locationRepository.save(LocationMapper.locationDtoToLocation(newEventDto.getLocation()));
        Event event = EventMapper.newEventDtoToEvent(user, category, location, created, newEventDto);
        //event.setState(EventState.PENDING);
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        Event eventSave = eventRepository.save(event);
        return EventMapper.eventToEventFullDto(eventSave);
    }

    @Override
    @Transactional
    public ParticipationResponseDto saveRequest(Long userId, Long eventId) {
        if (eventId == null) {
            throw new ValidationException("Отсутствует необходимый идентификатор");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));

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
    @Transactional
    public ParticipationResponseDto cancelRequestByUser(Long userId, Long requestId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Participation participation = requestRepository.findByIdAndRequester_Id(requestId, userId);
        if (participation == null) {
            throw new NotFoundException("Событие не найдено");
        }
        participation.setStatus(ParticipationStatus.CANCELED);
        Participation participationSave = requestRepository.save(participation);
        return ParticipationMapper.toParticipationResponseDto(participationSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationResponseDto> getRequestsByUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId);
        if (event == null) {
            throw new NotFoundException("Событие не найдено");
        }
        List<Participation> participations = requestRepository.findAllByEvent_Id(eventId);
        return participations.stream().map(ParticipationMapper::toParticipationResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatusByUserEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId);
        if (event == null) {
            throw new NotFoundException("Событие не найдено");
        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        Set<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();

        List<Participation> participations = requestRepository.findAllByEvent_IdAndIdIn(eventId, requestIds);

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            List<ParticipationResponseDto> participationResponseDtos = participations.stream()
                    .map(ParticipationMapper::toParticipationResponseDto)
                    .collect(Collectors.toList());
            participationResponseDtos.forEach(x -> x.setStatus(ParticipationStatus.CONFIRMED));
            eventRequestStatusUpdateResult.setConfirmedRequests(participationResponseDtos);
            return eventRequestStatusUpdateResult;
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит одобренных заявок");
        }
        for (Participation participation : participations) {
            Integer confirmedRequests = event.getConfirmedRequests();
            Integer participantLimit = event.getParticipantLimit();

            if (confirmedRequests >= participantLimit) {
                participation.setStatus(ParticipationStatus.REJECTED);
                requestRepository.save(participation);
                eventRequestStatusUpdateResult.getRejectedRequests()
                        .add(ParticipationMapper.toParticipationResponseDto(participation));
            }
            if (!participation.getStatus().equals(ParticipationStatus.PENDING)) {
                throw new ConflictException("Рассмотрение запросов возможно только в статусе ожидания");
            }
            if (eventRequestStatusUpdateRequest.getStatus() == ParticipationStatus.CONFIRMED) {
                participation.setStatus(ParticipationStatus.CONFIRMED);
                requestRepository.save(participation);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
                eventRequestStatusUpdateResult.getConfirmedRequests()
                        .add(ParticipationMapper.toParticipationResponseDto(participation));
            } else {
                participation.setStatus(ParticipationStatus.REJECTED);
                requestRepository.save(participation);
                eventRequestStatusUpdateResult.getRejectedRequests()
                        .add(ParticipationMapper.toParticipationResponseDto(participation));
            }
        }
        return eventRequestStatusUpdateResult;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsWithFilter(String text, List<Long> categories, Boolean paid,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   Boolean onlyAvailable, String sort, Integer from, Integer size,
                                                   HttpServletRequest request) {
//        - это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
//        - текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
//        - если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
//        - информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
//        - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики


        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
//        событие должно быть опубликовано
//        информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
//        информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Событие не найдено");
        }

        return null;
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
//        дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
//        событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
//        событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
        LocalDateTime now = LocalDateTime.now();
        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(now)) {
                throw new ValidationException("Начало события не может быть в прошлом");
            }
            if (now.plusHours(1).isAfter(updateEventAdminRequest.getEventDate())) {
                throw new ConflictException("Между датой публикации и началом события должен быть минимум 1 час");
            }
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Событие должно быть в статусе ожидания");
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(now);
            } else {
                event.setState(EventState.CANCELED);
            }
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationRepository.save(updateEventAdminRequest.getLocation());
            event.setLocation(location);
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        Event eventSave = eventRepository.save(event);
        return EventMapper.eventToEventFullDto(eventSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorOrderById(user, pageable);
        return EventMapper.eventsToEventShortDto(events);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByUserIdEventId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId);
        if (event == null) {
            throw new NotFoundException("Событие не найдено");
        }
        return EventMapper.eventToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUserIdEventId(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId);
        if (event == null) {
            throw new NotFoundException("Событие не найдено");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Параметры не удовлетворяют правилам редактирования события");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Запрос составлен некорректно");
        }
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Время не удовлетворяет правилам редактирования, должно быть min +2 часа");
            }
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(updateEventUserRequest.getLocation());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState(EventState.PENDING);
            } else if (updateEventUserRequest.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState(EventState.CANCELED);
            }
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        Event eventSave = eventRepository.save(event);
        return EventMapper.eventToEventFullDto(eventSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEventsAdmin(List<Long> users, EventState states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Specification<Event> specification = (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null && !users.isEmpty())
                predicates.add(root.get("initiator").in(users));
            if (states != null)
                predicates.add(cb.equal(root.get("state"), states));
            //predicates.add((root.get("state").in(states))); // возможно EventState
            if (categories != null && !categories.isEmpty())
                predicates.add(root.join("category", JoinType.INNER).get("id").in(categories));
            if (rangeStart != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            if (rangeEnd != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        List<Event> eventList = eventRepository.findAll(specification, pageable).getContent();
        return eventList.stream().map(EventMapper::eventToEventFullDto).collect(Collectors.toList());//views доделать
    }


    private void checkEventRequestConstraints(User user, Event event) {
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("Мероприятие достигло лимита участников");
        }
        if (!(event.getState().equals(EventState.PUBLISHED))) {
            throw new ConflictException("Мероприятие пока не опубликовано");
        }
        if (user.equals(event.getInitiator())) {
            throw new ConflictException(("Инициатор и участник - одно лицо"));
        }
    }

//    private List<Request> findConfirmedRequests(Event event) {
//        return requestRepository.findConfirmedRequests(event.getId());
//    }
//
//    private Map<Event, List<Request>> findConfirmedRequests(List<Event> events) {
//        List<Request> confirmedRequests =
//                requestRepository.findConfirmedRequests(events.stream().map(Event::getId).collect(Collectors.toList()));
//
//        return confirmedRequests.stream()
//                .collect(Collectors.groupingBy(Request::getEvent, Collectors.toList()));
//    }

//    private Map<Long, Integer> findViews(List<Event> events) {
//        return statsToStatsMap(
//                statsClient.getStats(events.stream().map(Event::getId).collect(Collectors.toList()))
//        );
//    }
//
//    private Map<Long, Integer> findViews(Set<Event> events) {
//        return statsToStatsMap(
//                statsClient.getStats(events.stream().map(Event::getId).collect(Collectors.toList()))
//        );
//    }
//
//    private Map<Long, Integer> findViews(Long eventId) {
//        return statsToStatsMap(
//                statsClient.getStats(List.of(eventId))
//        );
//    }
//
//    public static Map<Long, Long> statsToStatsMap(List<ViewStatsResponse> stats) {
//        return stats.stream()
//                .collect(
//                        toMap(stat -> Long.valueOf(stat.getUri().substring(8)), ViewStatsResponse::getHits)
//                );
//    }
}
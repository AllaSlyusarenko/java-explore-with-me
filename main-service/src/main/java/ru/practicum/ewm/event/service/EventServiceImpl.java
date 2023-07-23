package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.client.stats.StatsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import ru.practicum.ewm.dto.stats.ViewStatsResponse;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.LocationMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.repository.EventFilter;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utility.Constants;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;


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
    private ModelMapper mapper;

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
        if (LocalDateTime.parse(newEventDto.getEventDate(), Constants.formatterDate).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Указана неверная дата");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        Location location = locationRepository.save(LocationMapper.locationDtoToLocation(newEventDto.getLocation()));
        Event event = EventMapper.newEventDtoToEvent(user, category, location, newEventDto);
        event.setState(EventState.PENDING);
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
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
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return EventMapper.eventToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUserIdEventId(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (event.getState().equals(EventState.PENDING) && event.getState().equals(EventState.CANCELED)) {
            throw new ConflictException("Параметры не удовлетворяют правилам редактирования события");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Запрос составлен некорректно");
        }
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(mapper.map(updateEventUserRequest.getCategory(), Category.class));
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
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        // приходят 0
        Pageable pageable = PageRequest.of(from / size, size);
//        EventFilter filter = EventFilter.builder()
//                .users(users)
//                .states(states == null ? null : states.stream().map(EventState::valueOf).collect(Collectors.toList()))
//                .categories(categories)
//                .rangeStart(rangeStart)
//                .rangeEnd(rangeEnd)
//                .build();

        //List<Event> events = eventRepository.findAll(filter, pageable).toList();
        //Map<Long, Long> views = this.getViews(events);

        Specification<Event> specification = (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null && !users.isEmpty())
                predicates.add(root.get("initiator").in(users));
            if (states != null)
                predicates.add(cb.equal(root.get("state"), states));
//                predicates.add((root.get("state").in(states))); // возможно EventState
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
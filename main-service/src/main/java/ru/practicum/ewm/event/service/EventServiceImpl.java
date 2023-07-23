package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.client.stats.StatsClient;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.LocationMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utility.Constants;

import java.time.LocalDateTime;

import java.util.List;


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
        return EventMapper.eventToEventFullDto(eventSave, 0, 0);
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorOrderById(user, pageable);
        return EventMapper.eventsToEventShortDto(events);
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
package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utility.Constants;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@UtilityClass
public class EventMapper {
    public Event newEventDtoToEvent(User user, Category category, Location location, NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
//        event.setConfirmedRequests(newEventDto.getParticipantLimit());
        event.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), Constants.formatterDate));
        event.setInitiator(user);
        event.setLocation(location);
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setTitle(newEventDto.getTitle());
        event.setRequestModeration(newEventDto.getRequestModeration());
        return event;
    }

    public EventFullDto eventToEventFullDto(Event event, Integer confirmedRequests, Integer views) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(event.getCategory());
        eventFullDto.setConfirmedRequests(Objects.requireNonNullElse(confirmedRequests, 0));
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(event.getInitiator());
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(Objects.requireNonNullElse(views, 0));
        return eventFullDto;
    }
}

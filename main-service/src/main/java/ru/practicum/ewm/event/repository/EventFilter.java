package ru.practicum.ewm.event.repository;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.ewm.event.dto.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
//@Builder(toBuilder = true)
@Setter
@NoArgsConstructor
@ToString
public class EventFilter {
    private List<Long> users;
    private List<EventState> states;
    //private final String text;
    private List<Long> categories;
    //private final Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    //private final boolean onlyAvailable;
    private Integer from;
    private Integer size;
    private PageRequest page;

    public EventFilter(List<Long> users, List<EventState> states, List<Long> categories,
                       LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        this.users = users;
        this.states = states;
        this.categories = categories;
        if (rangeStart != null) {
            this.rangeStart = rangeStart;
        }
        if (rangeEnd != null) {
            this.rangeEnd = rangeEnd;
        }
        this.from = from;
        this.size = size;
        this.page = PageRequest.of(from, size, Sort.by("id"));
    }


    public boolean hasUsers(EventFilter params) {
        return params.getUsers() != null;
    }

    public boolean hasStates(EventFilter params) {
        return params.getStates() != null;
    }

    public boolean hasCategories(EventFilter params) {
        return params.getCategories() != null;
    }

    public boolean hasRangeStart(EventFilter params) {
        return params.getRangeStart() != null;
    }

    public boolean hasRangeEnd(EventFilter params) {
        return params.getRangeEnd() != null;
    }
}

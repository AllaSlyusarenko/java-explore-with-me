package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utility.Constants;


import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class EventFullDto { //Response
    private Long id;
    private String annotation;
    private Category category;
    private Integer confirmedRequests; //Количество одобренных заявок на участие в данном событии
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String description;
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private User initiator;
    private Location location;
    private Boolean paid; //надо ли платить
    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private LocalDateTime publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие
    private EventState state; //enum
    private String title; //Заголовок
    private Integer views; //Количество просмотрев события
}
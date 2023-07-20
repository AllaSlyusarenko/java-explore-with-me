package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.category.dto.CategoryResponseDto;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventFullDto { //Response
    private String annotation;
    private CategoryResponseDto category;
    private Integer confirmedRequests; //Количество одобренных заявок на участие в данном событии
    private String createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String description;
    private String eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid; //надо ли платить
    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private String publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие
    private String state; //enum
    private String title; //Заголовок
    private Integer views; //Количество просмотрев события
}
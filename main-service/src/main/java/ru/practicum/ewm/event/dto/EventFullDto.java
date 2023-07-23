package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.category.dto.CategoryResponseDto;
import ru.practicum.ewm.user.dto.UserShortDto;

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
    private CategoryResponseDto category;
    private Long confirmedRequests; //Количество одобренных заявок на участие в данном событии
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid; //надо ли платить
    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие
    private String state; //enum
    private String title; //Заголовок
    private Integer views; //Количество просмотрев события
}
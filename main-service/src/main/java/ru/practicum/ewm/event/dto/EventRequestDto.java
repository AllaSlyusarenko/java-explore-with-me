package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventRequestDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long categoryId;//id категории к которой относится событие
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    private String eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Location location;
    private Boolean paid; //надо ли платить default: false
    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
    @NotBlank
    @Size(min = 3, max = 120)
    private String title; //Заголовок
}
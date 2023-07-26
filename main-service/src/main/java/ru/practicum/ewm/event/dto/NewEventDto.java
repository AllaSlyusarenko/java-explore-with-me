package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @PositiveOrZero
    private Long category;//id категории к которой относится событие
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    //Обратите внимание: дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
    @NotNull
    private LocationDto location;
    @Nullable
    private Boolean paid = false; //надо ли платить default: false
    @Nullable
    private Integer participantLimit = 0; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @Nullable
    private Boolean requestModeration = true; //Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
    @NotBlank
    @Size(min = 3, max = 120)
    private String title; //Заголовок
}
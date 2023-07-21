package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.utility.Constants;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

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

    @NotNull
    @Future
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    //Обратите внимание: дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
    @NotNull
    private LocationDto location;
    private Boolean paid; //надо ли платить default: false
    @PositiveOrZero
    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
    @NotBlank
    @Size(min = 3, max = 120)
    private String title; //Заголовок
}
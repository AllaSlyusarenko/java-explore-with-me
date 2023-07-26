package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class UpdateEventUserRequest {
    // Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется

    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private Location location;
    private Boolean paid; //надо ли платить
    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие
    private String stateAction; //enum
    @Size(min = 3, max = 120)
    private String title; //Заголовок
}

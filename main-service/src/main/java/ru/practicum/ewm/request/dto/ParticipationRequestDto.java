package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParticipationRequestDto { //in
    private Long id; //Идентификатор заявки
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String created; // Дата и время создания заявки
    private Long event; //Идентификатор события
    private Long requester; // Идентификатор пользователя, отправившего заявку
    private RequestStatus status; //Статус заявки
}
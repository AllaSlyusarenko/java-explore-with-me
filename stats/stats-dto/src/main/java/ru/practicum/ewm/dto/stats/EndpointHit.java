package ru.practicum.ewm.dto.stats;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class EndpointHit {
    @NotBlank
    private String app; //Идентификатор сервиса, для которого записывается информация
    @NotBlank
    private String uri; //URI, для которого был осуществлен запрос
    @NotBlank
    private String ip; //IP-адрес пользователя, осуществившего запрос
    @NotBlank
    private String timestamp; // Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
}
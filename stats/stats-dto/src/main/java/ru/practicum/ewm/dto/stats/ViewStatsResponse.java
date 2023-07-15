package ru.practicum.ewm.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ViewStatsResponse {
    private String app; // Название сервиса
    private String uri; //URI сервиса
    private Integer hits; // Количество просмотров
}

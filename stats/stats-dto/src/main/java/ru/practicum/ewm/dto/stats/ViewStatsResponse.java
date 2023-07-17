package ru.practicum.ewm.dto.stats;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class ViewStatsResponse implements Serializable {
    private String app; // Название сервиса
    private String uri; //URI сервиса
    private Long hits; // Количество просмотров
}
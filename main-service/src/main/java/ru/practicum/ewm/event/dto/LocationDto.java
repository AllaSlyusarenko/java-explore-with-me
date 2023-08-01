package ru.practicum.ewm.event.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LocationDto {
    @NotNull
    private float lat;
    @NotNull
    private float lon;
}
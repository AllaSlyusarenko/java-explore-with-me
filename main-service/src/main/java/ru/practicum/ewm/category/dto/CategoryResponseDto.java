package ru.practicum.ewm.category.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryResponseDto {
    private Long id;
    private String name;
}
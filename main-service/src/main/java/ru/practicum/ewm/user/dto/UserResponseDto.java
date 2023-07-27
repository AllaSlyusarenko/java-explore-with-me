package ru.practicum.ewm.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
}
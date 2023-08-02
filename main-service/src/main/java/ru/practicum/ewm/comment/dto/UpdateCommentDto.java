package ru.practicum.ewm.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class UpdateCommentDto {
    @NotBlank
    @Size(max = 2000)
    private String text;
}
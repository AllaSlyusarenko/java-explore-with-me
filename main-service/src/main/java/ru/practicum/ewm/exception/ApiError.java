package ru.practicum.ewm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ApiError {
    private List<RuntimeException> errors = new ArrayList<>();
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends ValidationException {
    public ConflictException(String message) {
        super(message);
    }
}

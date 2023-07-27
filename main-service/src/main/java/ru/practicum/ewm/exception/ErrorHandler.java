package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "ru.practicum.ewm")
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException exception) {
        return new ErrorResponse(exception.getMessage(), "NOT_FOUND", ErrorStatus.NOT_FOUND);
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class, MissingRequestHeaderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException exception) {
        return new ErrorResponse(exception.getMessage(), "BAD_REQUEST", ErrorStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictValidationException(final DataIntegrityViolationException exception) {
        return new ErrorResponse(exception.getMessage(), "CONFLICT", ErrorStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable exception) {
        return new ErrorResponse(exception.getMessage(), "Unhandled exception.", ErrorStatus.INTERNAL_SERVER_ERROR);
    }
}
package ru.practicum.shareit.errors;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.EmailException;
import ru.practicum.shareit.exceptions.ItemOwnerException;
import ru.practicum.shareit.exceptions.NotFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailConflict(final EmailException e) {
        log.error("User cannot be created, {}", e.getMessage(), e);
        return new ErrorResponse("Error creating new user", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.error("Not found exception, {}", e.getMessage(), e);
        return new ErrorResponse("Not found exception", e.getMessage());
    }

    @ExceptionHandler({ItemOwnerException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOwnerException(final Exception e) {
        log.error("Owner exception, {}", e.getMessage(), e);
        return new ErrorResponse("Owner exception", e.getMessage());
    }
}

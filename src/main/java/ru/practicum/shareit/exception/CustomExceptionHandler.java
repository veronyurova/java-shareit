package ru.practicum.shareit.exception;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorResponse> fieldErrors = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            fieldErrors.add(new ErrorResponse("InvalidObject",
                    fieldError.getField() + " " + fieldError.getDefaultMessage()));
            log.warn("MethodArgumentNotValidException: Validation failed " +
                     "for value [{}] in field {}.{}; message: {}",
                     fieldError.getRejectedValue(), fieldError.getObjectName(),
                     fieldError.getField(), fieldError.getDefaultMessage());
        }
        return fieldErrors;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponse("UserNotFound", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmailAlreadyTakenException(EmailAlreadyTakenException e) {
        return new ErrorResponse("EmailAlreadyTaken", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse("InvalidObject", e.getMessage());
    }
}

@Getter
@AllArgsConstructor
class ErrorResponse {
    String error;
    String description;
}
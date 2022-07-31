package ru.practicum.shareit.exception;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponse("ValidationException",
                    error.getField() + " " +
                            error.getDefaultMessage()));
            log.warn("MethodArgumentNotValidException: {}.{} {}",
                     error.getObjectName(), error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException e) {
        List<ErrorResponse> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(new ErrorResponse("ValidationException",
                    violation.getPropertyPath().toString() + " " +
                            violation.getMessage()));
            log.warn("ConstraintViolationException: {} {}",
                     violation.getPropertyPath().toString(),
                     violation.getMessage());
        }
        return errors;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse("ValidationException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleSQLException(SQLException e) {
        return new ErrorResponse("SQLException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        return new ErrorResponse("EntityNotFound", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return new ErrorResponse("AccessDenied", e.getMessage());
    }

    @Getter
    @AllArgsConstructor
    class ErrorResponse {
        private String error;
        private String description;
    }
}
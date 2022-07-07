package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponse("ValidationException",
                       "поле " + error.getField() + " " + error.getDefaultMessage()));
            log.warn("MethodArgumentNotValidException: поле {}.{} {}",
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
        for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
            errors.add(new ErrorResponse("ValidationException",
                       "поле " + constraintViolation.getPropertyPath().toString() +
                       " " + constraintViolation.getMessage()));
            log.warn("ConstraintViolationException: поле {} {}",
                     constraintViolation.getPropertyPath().toString(),
                     constraintViolation.getMessage());
        }
        return errors;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponse("UserNotFound", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleItemNotFoundException(ItemNotFoundException e) {
        return new ErrorResponse("ItemNotFound", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return new ErrorResponse("AccessDenied", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleEmailAlreadyTakenException(EmailAlreadyTakenException e) {
        return new ErrorResponse("EmailAlreadyTaken", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse("ValidationException", e.getMessage());
    }
}

@Getter
@AllArgsConstructor
class ErrorResponse {
    String error;
    String description;
}
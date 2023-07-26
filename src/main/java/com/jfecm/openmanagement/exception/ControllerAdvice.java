package com.jfecm.openmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatusCode(HttpStatus.NOT_FOUND.value());
        message.setMessage(ex.getMessage());
        message.setRequestDescription(request.getDescription(false));
        message.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<ErrorMessage> internalExceptionHandler(InternalServerErrorException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        message.setMessage(ex.getMessage());
        message.setRequestDescription(request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        message.setMessage("Error");
        message.setRequestDescription(request.getDescription(false));

        // Get specific validation errors (fieldErrors)
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();

        // Add each error message to the list of fieldErrors
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }

        message.setFieldErrors(errorMessages);
        message.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NullProductDataException.class)
    public ResponseEntity<ErrorMessage> handleNullProductDataException(NullProductDataException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        message.setMessage(ex.getMessage());
        message.setRequestDescription(request.getDescription(false));
        message.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ProductNameAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> productNameAlreadyExistsException(ProductNameAlreadyExistsException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatusCode(HttpStatus.CONFLICT.value());
        message.setMessage(ex.getMessage());
        message.setRequestDescription(request.getDescription(false));
        message.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}

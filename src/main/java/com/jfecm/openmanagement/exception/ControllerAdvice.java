package com.jfecm.openmanagement.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.security.SignatureException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage("Validation failed for the request.");

        List<String> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        errorResponse.setFieldErrors(errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<ErrorMessage> invalidTokenException(InvalidTokenException ex, WebRequest request) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setRequestDescription(request.getDescription(false));
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = DuplicateUsernameException.class)
    public ResponseEntity<ErrorMessage> duplicateUsernameException(DuplicateUsernameException ex, WebRequest request) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setStatusCode(HttpStatus.CONFLICT.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setRequestDescription(request.getDescription(false));
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolation(EmailAlreadyExistsException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setStatusCode(HttpStatus.CONFLICT.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorMessage> registrationException(RegistrationException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setStatusCode(HttpStatus.CONFLICT.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = Exception.class)
    public ProblemDetail handleSecurityException(Exception e) {
        ProblemDetail error = null;
        if (e instanceof BadCredentialsException) {
            error = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), "Invalid credentials. Verify your username and password.");
        }

        if (e instanceof AccessDeniedException) {
            error = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), "You do not have permission to access this resource.");
        }

        if (e instanceof SignatureException) {
            error = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), "The JWT signature is invalid. Please check the provided JWT token.");
            error.setProperty("access_denied_reason", "Jwt Signature not valid.");
        }

        if (e instanceof ExpiredJwtException) {
            error = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), "The JWT token has expired. Please log in again to get a new token.");
        }

        return error;
    }

}

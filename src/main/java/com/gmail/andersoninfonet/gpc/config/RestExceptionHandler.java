package com.gmail.andersoninfonet.gpc.config;

import com.gmail.andersoninfonet.gpc.models.exceptions.ExceptionDetails;
import com.gmail.andersoninfonet.gpc.models.exceptions.GpcValidationExceptionDetails;
import org.hibernate.JDBCException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JDBCException.class)
    public ResponseEntity<ExceptionDetails> handlerGpcNotFoundException(
            final JDBCException ex) {
        return new ResponseEntity<>(
                new ExceptionDetails("Gpc Constraint Violation exception.",
                        HttpStatus.BAD_REQUEST.value(),
                        "Erro ao tentar realizar operação no banco de dados",
                        ex.getClass().getName(),
                        Instant.now(), null),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<GpcValidationExceptionDetails> validations = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(e ->
            validations.add(
                    new GpcValidationExceptionDetails(e.getField(), e.getDefaultMessage())));
        return new ResponseEntity<>(
                new ExceptionDetails("Gpc Bad Request exception.",
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        ex.getClass().getName(),
                        Instant.now(), validations),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String message = null;
        if(Objects.nonNull(ex.getMessage())) {
            message = ex.getMessage();
        } else {
            message = "Ocorreu um erro interno";
        }
        var exceptionDetails = new ExceptionDetails(ex.getCause().getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                message,
                ex.getClass().getName(),
                Instant.now(), null);

        return new ResponseEntity<>(exceptionDetails, headers, statusCode);
    }
}

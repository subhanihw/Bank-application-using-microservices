package com.nobita.microservices.customerservice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private final Map<Class<? extends Exception>, String> exceptionGuidMap = new HashMap<>();

    public CustomResponseEntityExceptionHandler() {
        exceptionGuidMap.put(InvalidFieldException.class, "6f72ba58-5fcc-41b1-8a1c-5a35a5bbbabd");
        exceptionGuidMap.put(CustomerNotFoundException.class, "d590aeff-93c8-4e34-9a2f-8b0e98f38c29");
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({CustomerNotFoundException.class, InvalidFieldException.class})
    public final ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) throws Exception
    {
        String guid = getGuidForException(ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Bad Request. Please Contact administrator.",
                request.getDescription(false), guid);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getFieldError().getDefaultMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    private String getGuidForException(Exception ex) {
        return exceptionGuidMap.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(ex))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseGet(this::generateGuid);
    }

    private String generateGuid() {
        return UUID.randomUUID().toString();
    }
}


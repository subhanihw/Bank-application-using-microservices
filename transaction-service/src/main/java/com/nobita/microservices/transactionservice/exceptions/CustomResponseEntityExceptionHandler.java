package com.nobita.microservices.transactionservice.exceptions;

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
        exceptionGuidMap.put(AccountNotFoundException.class, "23ef3a9d-8833-4ba1-bec4-1c5e06201309");
        exceptionGuidMap.put(AccountTypeAlreadyExistsException.class,"ba409f8b-3c67-43f5-8ade-4ca0b83bdfc4");
        exceptionGuidMap.put(CustomerNotFoundException.class, "d590aeff-93c8-4e34-9a2f-8b0e98f38c29");
        exceptionGuidMap.put(InvalidTransactionType.class, "e97bb121-2bdd-464c-a953-9e56cef45e71");
        exceptionGuidMap.put(InsufficientBalanceException.class, "3a38bc0a-5889-450e-84e9-9be5b74f46f6");
        exceptionGuidMap.put(MinBalanceException.class, "1822eb71-74f1-48b3-8dd1-b5fdb6ba4cb4");
        exceptionGuidMap.put(RequiresPanException.class, "83abd3cc-e326-49d2-82b3-459344bac28c");
        exceptionGuidMap.put(InvalidAccountTypeException.class, "44b95e76-952f-4444-8411-1bd0e981e492");
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidTransactionType.class, CustomerNotFoundException.class,
            AccountNotFoundException.class, AccountTypeAlreadyExistsException.class,
            InvalidAccountTypeException.class, RequiresPanException.class,
            MinBalanceException.class})
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


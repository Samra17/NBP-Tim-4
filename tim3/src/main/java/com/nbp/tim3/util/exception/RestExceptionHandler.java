package com.nbp.tim3.util.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Map<String, List<String>>> handleInvalidRequest(
            InvalidRequestException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage().toString());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<Map<String, List<String>>> handleEntityNotFound(EntityNotFoundException ex){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage().toString());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<Map<String, List<String>>> handleEmptyResultDataAccessNotFound(EmptyResultDataAccessException ex){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage().toString());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<Map<String, List<String>>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage().toString());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.NOT_FOUND);
    }



}
package com.nbp.tim3.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> handleUpdateValidationErrors(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(MenuItemNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>>  handleMenuItemDoesntExistException(MenuItemNotFoundException ex) {
        return new ResponseEntity<>(messageToMap(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>>  handleOrderDoesntExistException(OrderNotFoundException ex) {
        return new ResponseEntity<>(messageToMap(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(OrderPatchInvalidException.class)
    public ResponseEntity<Map<String, List<String>>> handleOrderPatchInvalidException(OrderPatchInvalidException ex) {
        return new ResponseEntity<>(messageToMap(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_MODIFIED);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    private Map<String, List<String>> messageToMap(String message) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        List<String> messageList = new ArrayList<>(); messageList.add(message);
        errorResponse.put("errors", messageList);
        return errorResponse;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<Map<String, List<String>>> handleEntityNotFound(EntityNotFoundException ex){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage().toString());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<Map<String, List<String>>> handleEmptyResultDataAccessNotFound(EmptyResultDataAccessException ex){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage().toString());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<Map<String, List<String>>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage().toString());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<Map<String, List<String>>> handleDuplicateEntry(DuplicateEntryException ex){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage().toString());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

}

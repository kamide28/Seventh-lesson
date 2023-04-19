package com.example.SeventhHomemork;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandle {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        Map<String, String> methodErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((methodError) -> {
            String fieldName = ((FieldError) methodError).getField();
            String methodErrorMassage = methodError.getDefaultMessage();
            methodErrors.put(fieldName, methodErrorMassage);
        });
        return methodErrors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {
        List<Object> constraintErrors = new ArrayList<Object>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            constraintErrors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        return constraintErrors;
    }

}

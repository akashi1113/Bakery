package com.csu.bakery.config;

import com.csu.bakery.controller.enums.AccountResponseCode;
import com.csu.bakery.dto.AccountResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AccountResponse<String>> handleValidationException(
            ConstraintViolationException ex) {

        String firstError = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(v -> v.getMessage())
                .orElse("Invalid parameter");

        return ResponseEntity.badRequest()
                .body(AccountResponse.error(AccountResponseCode.PARAM_INVALID, firstError));
    }
}
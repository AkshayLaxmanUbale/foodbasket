package com.demo.foodbasket.filters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.demo.foodbasket.contracts.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldname = ((FieldError) error).getField();
            String errMessage = error.getDefaultMessage();
            errors.put(fieldname, errMessage);
        });

        ErrorResponse errRes = ErrorResponse.builder()
                .errorMessage("Invalid Request.")
                .info(errors)
                .build();

        return ResponseEntity.badRequest().body(errRes);
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("msg", ex.getMessage());
        errors.put("type", ex.getClass().toString());

        ErrorResponse errRes = ErrorResponse.builder()
                .errorMessage("Internal Service Error.")
                .info(errors)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errRes);
    }
}

package com.express.security.exception;

import com.express.security.model.CommonResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException exception) {
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .errors(exception.getReason())
                .statusCode(exception.getStatus().toString())
                .build();

        return ResponseEntity
                .status(exception.getStatus())
                .body(commonResponse);
    }
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException exception) {
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .errors(exception.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(commonResponse);
    }
}

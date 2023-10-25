package com.prochnost.ecom.backend.controller.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> nullPointerExceptionHandler(Exception ex){
        String response = "error : Something went wrong" + ", code : " + HttpStatus.INTERNAL_SERVER_ERROR;;
        return ResponseEntity.ok(response);
    }
}

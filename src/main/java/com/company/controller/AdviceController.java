package com.company.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> handler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}

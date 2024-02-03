package com.company.controller;

import com.company.exp.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class AdviceController extends ResponseEntityExceptionHandler {


    @ExceptionHandler({BadRequestException.class, BadCredentialsException.class})
    public ResponseEntity<String> handler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}

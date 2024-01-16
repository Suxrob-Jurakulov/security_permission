package com.company.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.company.enums.AuthorityConstant.BOOK_ADD_AUTHORITY;
import static com.company.enums.AuthorityConstant.BOOK_GET_AUTHORITY;

@RestController
@RequestMapping("/book")
public class BookController {

    @GetMapping("/get/{id}")
    @PreAuthorize(BOOK_GET_AUTHORITY)
    public ResponseEntity<?> get(@PathVariable String id, Authentication authentication) {

        System.out.println(id);

        return ResponseEntity.ok(id);
    }

    @GetMapping("/add/{id}")
    @PreAuthorize(BOOK_ADD_AUTHORITY)
    public ResponseEntity<?> add(@PathVariable String id, Authentication authentication) {

        System.out.println(id);

        return ResponseEntity.ok(id);
    }
}

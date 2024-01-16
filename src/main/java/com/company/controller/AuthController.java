package com.company.controller;

import com.company.config.JwtService;
import com.company.dto.UserAccountDto;
import com.company.dto.UserDto;
import com.company.entity.UserEntity;
import com.company.exp.BadRequestException;
import com.company.form.LoginForm;
import com.company.form.UserForm;
import com.company.repository.UserRepository;
import com.company.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private void checkUsername(String username) {
        Optional<UserEntity> optional = userRepository.findByUsernameAndStateIsTrue(username);
        if (optional.isPresent()) {
            throw new BadRequestException("This username is busy");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserForm form) {

        checkUsername(form.getUsername());

        return ResponseEntity.ok().body(service.saveUser(form));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginForm form) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
        if (!auth.isAuthenticated()) {
            throw new BadRequestException("Invalid user request");
        }

        UserDto dto = service.login(form);

        return ResponseEntity.ok(dto);
    }


    @GetMapping("/profile")
    public ResponseEntity<UserAccountDto> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        String username = jwtService.extractUsername(authHeader.substring(7));
        return ResponseEntity.ok(service.getUser(username));
    }

}
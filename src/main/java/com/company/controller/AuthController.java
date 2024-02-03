package com.company.controller;

import com.company.dto.JwtDto;
import com.company.dto.ProfileDto;
import com.company.dto.TokenDto;
import com.company.exp.BadRequestException;
import com.company.form.ProfileForm;
import com.company.service.AuthService;
import com.company.service.TokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final TokensService tokensService;
    private final PasswordEncoder encoder;

    private ProfileDto check(String username) {
        return service.check(username);
    }


    @PostMapping("/register")
    public ResponseEntity<ProfileDto> register(@RequestBody ProfileForm form) {

        // Check profile in Database
        ProfileDto profile = check(form.getUsername());
        if (profile != null) {
            throw new BadRequestException("This username is busy!");
        }

        return ResponseEntity.ok().body(service.add(form));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody ProfileForm form) {

        // Check profile in Database
        ProfileDto profile = check(form.getUsername());
        if (profile == null) {
            throw new BadRequestException("User not found!");
        }

        // Check password
        if (!encoder.matches(form.getPassword(), profile.getPassword())) {
            throw new BadRequestException("Wrong password!");
        }

        form.setProfile(profile);

        // Create and save token
        TokenDto dto = tokensService.add(form.getProfile());

        return ResponseEntity.ok(new JwtDto(dto.getUid(), dto.getAccessToken(), dto.getRefreshToken()));
    }

}
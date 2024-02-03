package com.company.controller;

import com.company.dto.JwtDto;
import com.company.dto.ProfileDto;
import com.company.dto.TokenDto;
import com.company.helper.JwtHelper;
import com.company.form.TokenForm;
import com.company.service.AuthService;
import com.company.service.TokensService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokensService tokensService;
    private final AuthService service;
    private final JwtHelper jwtHelper;

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody TokenForm form) {

        // Parse token
        String tokenId;
        try {
            tokenId = jwtHelper.extractId(form.getRefreshToken());
        } catch (JwtException e) {
            throw new BadCredentialsException("Token is not valid!");
        }

        // Check token to expired
        if (jwtHelper.isTokenExpired(form.getRefreshToken())) {
            throw new BadCredentialsException("Token is expired!");
        }

        // Check token to type
        if (!jwtHelper.extractType(form.getRefreshToken()).equals("refresh")) {
            throw new BadCredentialsException("Token type is not refresh!");
        }

        // Check has token in database
        TokenDto refreshToken = tokensService.getRefreshById(tokenId);
        if (refreshToken == null) {
            throw new BadCredentialsException("Token is deleted!");
        }

        // Check has user in database
        ProfileDto profile = service.getById(jwtHelper.extractUserId(form.getRefreshToken()));
        if (profile == null) {
            throw new BadCredentialsException("User not found!");
        }

        // Set auth to form
        TokenDto dto = tokensService.add(profile);

        return ResponseEntity.ok(new JwtDto(dto.getUid(), dto.getAccessToken(), dto.getRefreshToken()));
    }

}

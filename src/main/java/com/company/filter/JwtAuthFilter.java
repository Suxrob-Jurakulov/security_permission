package com.company.filter;

import com.company.config.CustomUserDetails;
import com.company.config.CustomUserDetailsService;
import com.company.dto.AuthBasicDto;
import com.company.exp.BadRequestException;
import com.company.helper.JsonHelper;
import com.company.helper.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        UserDetails userDetails;
        try {

            // Authentication Basic
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                AuthBasicDto basic = getBasic(authHeader);

                if (basic.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = customUserDetailsService.loadUserByUsername(basic.getUsername());

                    if (!userDetails.getPassword().equals(basic.getPassword())) {
                        throw new BadRequestException("Password is incorrect");
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }

                filterChain.doFilter(request, response);
            }

            // Authentication bearer
            else if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                String profileId = jwtHelper.extractUserId(token);

                if (profileId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = customUserDetailsService.loadUserByUsername(profileId);

                    if (jwtHelper.validateToken(token, (CustomUserDetails) userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                filterChain.doFilter(request, response);

            } else {
                filterChain.doFilter(request, response);
            }

        } catch (UsernameNotFoundException | BadRequestException | BadCredentialsException | ExpiredJwtException ex) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(JsonHelper.get(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage())));

        }
    }

    private AuthBasicDto getBasic(String authHeader) {
        AuthBasicDto basic = new AuthBasicDto("", "");
        if (authHeader != null && !authHeader.isBlank()) {
            String decodedStr = null;
            try {
                decodedStr = new String(Base64.getDecoder().decode(authHeader.trim().substring(6)), StandardCharsets.UTF_8);

            } catch (Exception ignore) {
            }
            if (decodedStr != null) {
                String[] credentialsArray = decodedStr.split(":", 2);
                if (credentialsArray.length > 1) {
                    String username = credentialsArray[0];
                    String password = credentialsArray[1];
                    if (username != null && !username.isBlank()) {
                        basic.setUsername(username);
                    }

                    if (password != null && !password.isBlank()) {
                        basic.setPassword(password);
                    }
                }
            }
        }

        return basic;
    }
}
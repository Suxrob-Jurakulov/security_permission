package com.company.service;

import com.company.config.JwtService;
import com.company.dto.UserAccountDto;
import com.company.dto.UserDto;
import com.company.entity.UserEntity;
import com.company.enums.Role;
import com.company.form.LoginForm;
import com.company.form.UserForm;
import com.company.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto saveUser(UserForm form) {
        UserEntity entity = new UserEntity();

        entity.setUsername(form.getUsername());
        entity.setPassword(passwordEncoder.encode(form.getPassword()));
        entity.setRole(Role.ROLE_USER);
        entity.setCreateAt(LocalDateTime.now());
        entity.setModules(form.getModules());
        entity.setState(true);

        repository.save(entity);
        return map(entity);
    }


    @Override
    public UserDto login(LoginForm form) {
        String jwt = jwtService.generateToken(form.getUsername());

        UserDto dto = new UserDto();
        dto.setUsername(form.getUsername());
        dto.setJwt(jwt);
        return dto;
    }

    @Override
    public UserAccountDto getUser(String username) {
        Optional<UserEntity> opt = repository.findByUsernameAndStateIsTrue(username);
        if (opt.isPresent()) {
            UserEntity entity = opt.get();

            UserAccountDto dto = new UserAccountDto();

            dto.setId(entity.getId());
            dto.setUsername(entity.getUsername());
            dto.setCreatedAt(entity.getCreateAt());
            dto.setModules(entity.getModules());
            return dto;
        }
        return null;
    }


    // MAPPER
    private UserDto map(UserEntity entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setCreatedAt(entity.getCreateAt());
        dto.setModules(entity.getModules());
        return dto;
    }

}
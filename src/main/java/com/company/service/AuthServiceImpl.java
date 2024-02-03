package com.company.service;

import com.company.domains.Profile;
import com.company.dto.ProfileDto;
import com.company.enums.Role;
import com.company.form.ProfileForm;
import com.company.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ProfileRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileDto check(String username) {
        Optional<Profile> profileOpt = repository.findByUsernameAndDeletedIsFalse(username);
        return profileOpt.map(this::map).orElse(null);
    }

    @Override
    public ProfileDto getById(String id) {
        Optional<Profile> profileOpt = repository.findByIdAndDeletedIsFalse(id);

        return profileOpt.map(this::map).orElse(null);
    }

    @Override
    public ProfileDto add(ProfileForm form) {
        Profile profile = new Profile();

        profile.setId(UUID.randomUUID().toString());
        profile.setUsername(form.getUsername());
        profile.setPassword(passwordEncoder.encode(form.getPassword()));
        profile.setRole(Role.ROLE_USER);
        profile.setCreatedDate(LocalDateTime.now());
        profile.setModules(form.getModules());
        profile.setDeleted(false);

        repository.save(profile);
        return map(profile);
    }

    // MAPPER
    private ProfileDto map(Profile profile) {
        ProfileDto dto = new ProfileDto();

        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setPassword(profile.getPassword());
        dto.setCreatedDate(profile.getCreatedDate());
        dto.setModules(profile.getModules());

        return dto;
    }

}
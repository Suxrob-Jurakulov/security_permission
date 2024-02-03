package com.company.service;

import com.company.dto.ProfileAccountDto;
import com.company.dto.ProfileDto;
import com.company.form.LoginForm;
import com.company.form.ProfileForm;

public interface AuthService {

    ProfileDto check(String username);

    ProfileDto add(ProfileForm form);

    ProfileDto getById(String id);
}
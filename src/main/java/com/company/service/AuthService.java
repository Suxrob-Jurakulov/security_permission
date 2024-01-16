package com.company.service;

import com.company.dto.UserAccountDto;
import com.company.dto.UserDto;
import com.company.form.LoginForm;
import com.company.form.UserForm;

public interface AuthService {

    UserDto saveUser(UserForm form);

    UserDto login(LoginForm form);

    UserAccountDto getUser(String username);

}
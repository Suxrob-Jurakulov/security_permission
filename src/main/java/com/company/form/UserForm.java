package com.company.form;

import com.company.dto.ProfileModuleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {

    private String username;
    private String password;

    private List<ProfileModuleDto> modules;

}
package com.company.form;

import com.company.dto.ProfileDto;
import com.company.dto.ProfileModuleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileForm {

    private String username;
    private String password;
    private List<ProfileModuleDto> modules;

    // Other element
    private ProfileDto profile;
}

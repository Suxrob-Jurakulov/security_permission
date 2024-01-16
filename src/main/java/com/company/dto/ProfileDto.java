package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

    private Integer id;
    private String username;
    private List<ProfileModuleDto> modules;

    // Other elements
    private String jwt;
}

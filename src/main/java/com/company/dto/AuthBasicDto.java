package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

@Data
@AllArgsConstructor
@AutoConfigurationPackage
public class AuthBasicDto {

    private String username;
    private String password;
}

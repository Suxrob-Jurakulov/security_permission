package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private List<ProfileModuleDto> modules;

    // Other elements
    private String jwt;

}
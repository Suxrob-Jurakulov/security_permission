package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {

    private String id;
    private String uid;

    private String accessToken;
    private Date accessExpirationDate;

    private String refreshToken;
    private Date refreshExpirationDate;
}

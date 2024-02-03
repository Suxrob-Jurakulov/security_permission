package com.company.service;

import com.company.dto.ProfileDto;
import com.company.dto.TokenDto;

public interface TokensService {
    TokenDto add(ProfileDto dto);

    TokenDto getRefreshById(String tokenId);
}

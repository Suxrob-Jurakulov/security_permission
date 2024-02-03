package com.company.service;

import com.company.domains.Tokens;
import com.company.dto.ProfileDto;
import com.company.dto.TokenDto;
import com.company.helper.JwtHelper;
import com.company.repository.TokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokensServiceImpl implements TokensService {

    @Autowired
    private TokensRepository repository;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public TokenDto add(ProfileDto dto) {

        Tokens token = new Tokens();

        token.setId(UUID.randomUUID().toString());
        token.setUid(dto.getId());
        token.setAccessToken(jwtHelper.generateAccessToken(dto.getId(), token.getId()));
        token.setAccessExpirationDate(jwtHelper.extractExpiration(token.getAccessToken()));

        token.setRefreshToken(jwtHelper.generateRefreshToken(dto.getId(), token.getId()));
        token.setRefreshExpirationDate(jwtHelper.extractExpiration(token.getRefreshToken()));

        token.setDeleted(false);
        token.setCreatedDateTime(new Date());

        token = repository.save(token);

        return map(token);
    }

    @Override
    public TokenDto getRefreshById(String tokenId) {
        Optional<Tokens> obj = repository.findByIdAndRefreshExpirationDateAfterAndDeletedIsFalse(tokenId, new Date());
        return obj.map(this::map).orElse(null);
    }


    // MAPPER
    private TokenDto map(Tokens token) {

        TokenDto dto = new TokenDto();

        dto.setUid(token.getUid());
        dto.setAccessToken(token.getAccessToken());
        dto.setRefreshToken(token.getRefreshToken());

        return dto;
    }

}

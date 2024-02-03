package com.company.repository;

import com.company.domains.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<Tokens, String> {
    Optional<Tokens> findByIdAndRefreshExpirationDateAfterAndDeletedIsFalse(String tokenId, Date date);
}

package com.company.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tokens {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false, updatable = false)
    private String uid;

    @Column(nullable = false, length = 1000)
    private String accessToken;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private Date accessExpirationDate;

    @Column(nullable = false, length = 1000)
    private String refreshToken;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private Date refreshExpirationDate;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private Date createdDateTime;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(columnDefinition = "timestamp with time zone")
    private Date deletedDateTime;
}

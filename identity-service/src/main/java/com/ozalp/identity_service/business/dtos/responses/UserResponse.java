package com.ozalp.identity_service.business.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserResponse {

    private UUID id;

    private String email;

    private String passwordHash;

    private String role;

    private boolean active;

    private LocalDateTime createdAt;
}

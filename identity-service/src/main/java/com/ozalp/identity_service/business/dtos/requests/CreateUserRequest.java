package com.ozalp.identity_service.business.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateUserRequest {

    private String email;
    private String password;

}

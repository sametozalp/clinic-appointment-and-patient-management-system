package com.ozalp.identity_service.business.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
    String email;
    String password;
}

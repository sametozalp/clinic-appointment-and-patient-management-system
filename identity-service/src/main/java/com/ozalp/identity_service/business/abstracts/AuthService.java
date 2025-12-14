package com.ozalp.identity_service.business.abstracts;

import com.ozalp.identity_service.business.dtos.requests.CreateUserRequest;
import com.ozalp.identity_service.business.dtos.requests.LoginUserRequest;
import com.ozalp.identity_service.business.dtos.responses.UserResponse;

public interface AuthService {

    UserResponse register(CreateUserRequest request);

    UserResponse login(LoginUserRequest request);
}

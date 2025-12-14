package com.ozalp.identity_service.business.mappers;

import com.ozalp.identity_service.business.dtos.requests.CreateUserRequest;
import com.ozalp.identity_service.business.dtos.responses.UserResponse;
import com.ozalp.identity_service.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);
}

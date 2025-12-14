package com.ozalp.identity_service.business.concretes;

import com.ozalp.identity_service.business.abstracts.AuthService;
import com.ozalp.identity_service.business.dtos.requests.CreateUserRequest;
import com.ozalp.identity_service.business.dtos.requests.LoginUserRequest;
import com.ozalp.identity_service.business.dtos.responses.UserResponse;
import com.ozalp.identity_service.business.enums.Role;
import com.ozalp.identity_service.business.mappers.UserMapper;
import com.ozalp.identity_service.entities.User;
import com.ozalp.identity_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthManager implements AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public UserResponse register(CreateUserRequest request) {
        User user = mapper.toEntity(request);
        user.setActive(true);
        user.setRole(Role.PATIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return mapper.toResponse(repository.save(user));
    }

    @Override
    public UserResponse registerAdmin(CreateUserRequest request) {
        User user = mapper.toEntity(request);
        user.setActive(true);
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return mapper.toResponse(repository.save(user));

    }

    public UserResponse login(LoginUserRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public UserResponse getUser(String email) {
        Optional<User> user = repository.findByEmail(email);

        if (user.isPresent())
            return mapper.toResponse(user.get());

        return null;
    }
}

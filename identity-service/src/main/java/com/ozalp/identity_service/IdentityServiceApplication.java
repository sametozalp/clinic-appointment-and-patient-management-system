package com.ozalp.identity_service;

import com.ozalp.identity_service.business.abstracts.AuthService;
import com.ozalp.identity_service.business.dtos.requests.CreateUserRequest;
import com.ozalp.identity_service.business.dtos.responses.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class IdentityServiceApplication implements CommandLineRunner {

    private final AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        UserResponse admin = authService.getUser("admin@gmail.com");
        if (admin == null) {
            CreateUserRequest request = new CreateUserRequest("admin@gmail.com", "123456");
            authService.registerAdmin(request);
        }
    }
}

package com.ozalp.identity_service.controllers;

import com.ozalp.identity_service.business.abstracts.AuthService;
import com.ozalp.identity_service.business.dtos.requests.CreateUserRequest;
import com.ozalp.identity_service.business.dtos.requests.LoginUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}

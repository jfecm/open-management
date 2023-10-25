package com.jfecm.openmanagement.auth.controller;

import com.jfecm.openmanagement.auth.request.AuthRegisterRequest;
import com.jfecm.openmanagement.auth.request.AuthRequest;
import com.jfecm.openmanagement.auth.response.AuthResponse;
import com.jfecm.openmanagement.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "Operations for users")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(service.signIn(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody AuthRegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

}

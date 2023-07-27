package com.jfecm.openmanagement.security.auth.controller;

import com.jfecm.openmanagement.security.auth.model.ERole;
import com.jfecm.openmanagement.security.auth.model.RoleEntity;
import com.jfecm.openmanagement.security.auth.model.UserEntity;
import com.jfecm.openmanagement.security.auth.repository.UserRepository;
import com.jfecm.openmanagement.security.auth.request.CreateUserDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto user) {
        Set<RoleEntity> roles = user
                .getRoles()
                .stream()
                .map(role -> RoleEntity.builder().name(ERole.valueOf(role)).build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/delete-user")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@RequestParam String id) {
        userRepository.deleteById(Long.parseLong(id));
        return " The user with id has been deleted - id:".concat(id);
    }

}

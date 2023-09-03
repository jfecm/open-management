package com.jfecm.openmanagement.auth.service;

import com.jfecm.openmanagement.auth.request.AuthRegisterRequest;
import com.jfecm.openmanagement.auth.request.AuthRequest;
import com.jfecm.openmanagement.auth.response.AuthResponse;
import com.jfecm.openmanagement.exception.DuplicateUsernameException;
import com.jfecm.openmanagement.security.jwt.JwtService;
import com.jfecm.openmanagement.user.model.User;
import com.jfecm.openmanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Allows you to register a new user.
     * @param request User data.
     * @return Token.
     */
    public AuthResponse register(AuthRegisterRequest request) {
        verifyDuplicateUsername(request.getUsername());

        User user = User
                .builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .email(request.getEmail())
                .build();

        repository.save(user);

        String jwtToken = jwtService.getToken(user);

        return AuthResponse
                .builder()
                .accessToken(jwtToken)
                .build();
    }

    /**
     * Allows the user to log-in.
     * @param request Username and password.
     * @return Token.
     */
    public AuthResponse signIn(AuthRequest request) {
        User user = verifyUserExists(request);

        verifyUserPassword(request, user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String jwtToken  = jwtService.getToken(user);

        return AuthResponse
                .builder()
                .accessToken(jwtToken)
                .build();
    }

    private User verifyUserExists(AuthRequest request) {
        Optional<User> user = repository.findByUsername(request.getUsername());

        if (user.isEmpty()) {
            throw new BadCredentialsException("The specified user is not registered.");
        }

        return user.get();
    }

    private void verifyUserPassword(AuthRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("The specified password is not correct.");
        }
    }

    private void verifyDuplicateUsername(String username) {
        Optional<User> user = repository.findByUsername(username);

        if (user.isPresent()) {
            throw new DuplicateUsernameException("The specified username is already in use.");
        }

    }
}

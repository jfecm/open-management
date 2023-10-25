package com.jfecm.openmanagement.auth.service;

import com.jfecm.openmanagement.auth.request.AuthRegisterRequest;
import com.jfecm.openmanagement.auth.request.AuthRequest;
import com.jfecm.openmanagement.auth.response.AuthResponse;
import com.jfecm.openmanagement.exception.DuplicateUsernameException;
import com.jfecm.openmanagement.exception.EmailAlreadyExistsException;
import com.jfecm.openmanagement.exception.RegistrationException;
import com.jfecm.openmanagement.security.jwt.JwtService;
import com.jfecm.openmanagement.user.model.User;
import com.jfecm.openmanagement.user.repository.UserRepository;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
        try {
        verifyDuplicateUsername(request.getUsername());
        verifyDuplicateEmail(request.getEmail());

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
        } catch (IllegalArgumentException e) {
            throw new RegistrationException(e.getMessage());
        } catch (ConstraintViolationException  ex) {
            StringBuilder validationErrors = new StringBuilder("Validation failed for the following fields:\n");

            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                validationErrors.append("- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
            }

            throw new RegistrationException(validationErrors.toString());
        }
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

    private void verifyDuplicateEmail(String email) {
        if (repository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Duplicate email. Please choose another email.");
        }
    }
}

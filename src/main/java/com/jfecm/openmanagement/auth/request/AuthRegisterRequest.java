package com.jfecm.openmanagement.auth.request;

import com.jfecm.openmanagement.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterRequest {
    private String email;
    private String username;
    private String password;
    private Role role;
}
package com.jfecm.openmanagement.user.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum Role {
    ADMIN,
    USER,
    SELLER,
    INVENTORY_MANAGER,
    REPAIR_TECHNICIAN,
    ATM,
    CUSTOMER;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of((new SimpleGrantedAuthority("ROLE_" + this.name())));
    }
}

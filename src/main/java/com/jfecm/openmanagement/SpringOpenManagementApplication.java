package com.jfecm.openmanagement;

import com.jfecm.openmanagement.security.auth.model.ERole;
import com.jfecm.openmanagement.security.auth.model.RoleEntity;
import com.jfecm.openmanagement.security.auth.model.UserEntity;
import com.jfecm.openmanagement.security.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpringOpenManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringOpenManagementApplication.class, args);
    }


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Bean
    CommandLineRunner init() {
        return args -> {
            UserEntity userEntity = UserEntity.builder()
                    .email("admin_jfecm@gmail.com")
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(Set.of(
                            RoleEntity.builder()
                                    .name(ERole.valueOf(ERole.ADMIN.name()))
                                    .build()))
                    .build();
            userRepository.save(userEntity);
        };
    }
}

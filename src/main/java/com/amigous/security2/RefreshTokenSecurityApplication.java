package com.amigous.security2;

import com.amigous.security2.entity.Role;
import com.amigous.security2.entity.User;
import com.amigous.security2.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class RefreshTokenSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefreshTokenSecurityApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_SUPERVISOR"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));

            userService.saveUser(new User(null, "Devi", "Devi", "Devi", new ArrayList<>()));
            userService.saveUser(new User(null, "Kemal", "Kemal", "Kemal", new ArrayList<>()));
            userService.saveUser(new User(null, "Ketsar", "Ketsar", "Ketsar", new ArrayList<>()));
            userService.saveUser(new User(null, "Nabil", "Nabil", "Nabil", new ArrayList<>()));

            userService.addRoleToUser("Devi", "ROLE_USER");
            userService.addRoleToUser("Kemal", "ROLE_USER");
            userService.addRoleToUser("Kemal", "ROLE_ADMIN");
            userService.addRoleToUser("Ketsar", "ROLE_SUPERVISOR");
            userService.addRoleToUser("Ketsar", "ROLE_MANAGER");
            userService.addRoleToUser("Nabil", "ROLE_ADMIN");
            userService.addRoleToUser("Nabil", "ROLE_SUPERVISOR");
            userService.addRoleToUser("Nabil", "ROLE_MANAGER");
        };
    }

}

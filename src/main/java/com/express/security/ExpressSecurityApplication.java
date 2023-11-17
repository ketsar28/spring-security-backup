package com.express.security;


//import com.express.security.entity.Role;
import com.express.security.entity.User;
//import com.express.security.repository.RoleRepository;
import com.express.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.UUID;

@SpringBootApplication
public class ExpressSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpressSecurityApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		return args -> {
//			if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
//
//			Role adminRole = roleRepository.save(new Role(UUID.randomUUID().toString(), "ADMIN"));
//			roleRepository.save(new Role(UUID.randomUUID().toString(), "USER"));
//			HashSet<Role> roles = new HashSet<>();
//			roles.add(adminRole);
//
//
//			User user = new User(UUID.randomUUID().toString(), "admin", passwordEncoder.encode("password"), roles);
//			userRepository.save(user);
//		};
//	}

}

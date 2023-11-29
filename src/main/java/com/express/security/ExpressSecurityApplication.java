package com.express.security;

import com.express.security.entity.Role;
import com.express.security.repository.RoleRepository;
import com.express.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class ExpressSecurityApplication {
	private final RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ExpressSecurityApplication.class, args);
	}

	@Bean
	public ApplicationRunner run() {
		return args -> {
			if(roleRepository.count() == 0) {
				roleRepository.save(new Role(null, "ROLE_USER"));
				roleRepository.save(new Role(null, "ROLE_ADMIN"));
			}
		};
	}
}

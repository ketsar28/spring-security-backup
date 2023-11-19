package com.amigous.security2.repository;

import com.amigous.security2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long>{
    Role findByName(String name);
}

package com.example.dev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dev.model.Role;
import com.example.dev.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);

    boolean existsByRole(Role role);

}

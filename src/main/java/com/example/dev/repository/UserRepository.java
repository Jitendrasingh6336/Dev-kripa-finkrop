package com.example.dev.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev.model.Role;
import com.example.dev.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);

    boolean existsByRole(Role role);
    
    @Query("""
            SELECT u FROM User u
            WHERE u.isDeleted = false
              AND (
                   :search IS NULL OR
                   LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR
                   LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
                   LOWER(u.role) LIKE LOWER(CONCAT('%', :search, '%'))
              )""")
    Page<User> findUsers(@Param("search") String search, Pageable pageable);

	Optional<User> findByIdAndIsDeleted(String id, Boolean false1);
	
	long countByIsDeletedFalse();

    long countByIsDeletedFalseAndIsActiveTrue();

    long countByIsDeletedFalseAndIsActiveFalse();

}

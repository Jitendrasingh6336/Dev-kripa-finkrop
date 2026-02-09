package com.example.dev.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev.model.CustomerQuery;

public interface CustomerQueryRepository extends JpaRepository<CustomerQuery, String>{

	Optional<CustomerQuery> findByIdAndIsDeleted(String id, Boolean false1);
	
	@Query("""
	        SELECT c FROM CustomerQuery c
	        WHERE c.isDeleted = false
	          AND (
	               :search IS NULL OR
	               LOWER(c.username) LIKE LOWER(CONCAT('%', :search, '%')) OR
	               LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
	               LOWER(c.message) LIKE LOWER(CONCAT('%', :search, '%')) OR
	               LOWER(c.queryStatus) LIKE LOWER(CONCAT('%', :search, '%')) OR
	               LOWER(c.serviceType) LIKE LOWER(CONCAT('%', :search, '%'))
	          )
	    """)
	    Page<CustomerQuery> findCustomerQueries(
	            @Param("search") String search,
	            Pageable pageable
	    );
	
	

}

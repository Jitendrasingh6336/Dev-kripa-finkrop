package com.example.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev.model.Employee1;

public interface Employee1Repository extends JpaRepository<Employee1, String>{

	 Page<Employee1> findByIsDeletedFalse(Pageable pageable);


	    // search employee by name, email, phone etc.
	    @Query("""
	           SELECT e FROM Employee1 e
	           WHERE e.isDeleted = false AND
	           (
	               LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
	               OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
	               OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
	               OR LOWER(e.phone) LIKE LOWER(CONCAT('%', :search, '%'))
	           )
	           """)
	    Page<Employee1> searchEmployees(@Param("search") String search,
	                                   Pageable pageable);


		Optional<Employee1> findByIdAndIsDeleted(String id, Boolean false1);
		
		Optional<Employee1> findByEmailIgnoreCase(String email);

		Optional<Employee1> findByUsernameIgnoreCase(String username);
		
		List<Employee1> findByIsDeletedFalse();
		
	    @Query("SELECT e FROM Employee1 e WHERE e.isDeleted = false")
	    List<Employee1> findAllActiveEmployees();
}

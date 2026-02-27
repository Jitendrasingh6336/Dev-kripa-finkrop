package com.example.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

	
	 // get all non-deleted employees
    Page<Employee> findByIsDeletedFalse(Pageable pageable);


    // search employee by name, email, phone etc.
    @Query("""
           SELECT e FROM Employee e
           WHERE e.isDeleted = false AND
           (
               LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(e.phone) LIKE LOWER(CONCAT('%', :search, '%'))
           )
           """)
    Page<Employee> searchEmployees(@Param("search") String search,
                                   Pageable pageable);


	Optional<Employee> findByIdAndIsDeleted(String id, Boolean false1);
	
	Optional<Employee> findByEmailIgnoreCase(String email);

	Optional<Employee> findByUsernameIgnoreCase(String username);
	
	List<Employee> findByIsDeletedFalse();
	
    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false")
    List<Employee> findAllActiveEmployees();
}

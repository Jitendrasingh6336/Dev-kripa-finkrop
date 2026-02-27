package com.example.dev.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.dev.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, String> {

	Boolean existsByNameAndIsDeletedFalse(String name);

	Optional<Department> findByIdAndIsDeleted(String id, Boolean isDeleted);

	@Query("""
			 SELECT d FROM Department d
			 WHERE d.isDeleted = false
			 AND LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))
			""")
	Page<Department> searchDepartment(String search, Pageable pageable);

	Optional<Department> findByAndIdAndIsDeleted(String name, Boolean false1);

	Optional<Department> findByNameIgnoreCaseAndIsDeletedFalse(String name);

	Page<Department> findByIsDeletedFalse(Pageable pageable);

	// search by name or description with pagination
	@Query("""
			SELECT d FROM Department d
			WHERE d.isDeleted = false AND
			(
			    LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%'))
			)
			""")
	Page<Department> searchDepartments(@Param("search") String search, Pageable pageable);

	

}

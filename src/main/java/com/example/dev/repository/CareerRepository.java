package com.example.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev.model.CareerApplication;
import com.example.dev.model.CareerStatus;

public interface CareerRepository extends JpaRepository<CareerApplication, String> {
	
	@Query("""
		    SELECT c FROM CareerApplication c
		    WHERE c.isDeleted = false
		      AND (
		           :search IS NULL OR
		           LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
		           LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
		           LOWER(c.phone) LIKE LOWER(CONCAT('%', :search, '%')) OR
		           LOWER(c.position) LIKE LOWER(CONCAT('%', :search, '%')) OR
		           LOWER(c.status) LIKE LOWER(CONCAT('%', :search, '%'))
		      )
		""")
		Page<CareerApplication> findCareerApplications(
		        @Param("search") String search,
		        Pageable pageable
		);

	
	long countByStatusAndIsDeletedFalse(CareerStatus status);

	long countByStatusInAndIsDeletedFalse(List<CareerStatus> statuses);
	
	boolean existsByEmailAndIsDeletedFalse(String email);

	boolean existsByPhoneAndIsDeletedFalse(String phone);


	Optional<CareerApplication> findByIdAndIsDeleted(String id, Boolean isDeleted);




}

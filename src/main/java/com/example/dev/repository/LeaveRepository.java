package com.example.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev.model.Leave;

public interface LeaveRepository extends JpaRepository<Leave, String>{

	
	 @Query("""
	            SELECT l FROM Leave l
	            WHERE l.isDeleted = false
	              AND (
	                   :search IS NULL OR
	                   LOWER(l.reason) LIKE LOWER(CONCAT('%', :search, '%')) OR
	                   LOWER(l.leaveType) LIKE LOWER(CONCAT('%', :search, '%')) OR
	                   LOWER(l.status) LIKE LOWER(CONCAT('%', :search, '%')) OR
	                   LOWER(l.employee.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
	                   LOWER(l.employee.email) LIKE LOWER(CONCAT('%', :search, '%'))
	              )
	            """)
	    Page<Leave> findLeaves(@Param("search") String search,Pageable pageable );


	    // Find by ID and isDeleted false
	    Optional<Leave> findByIdAndIsDeleted(String id, Boolean isDeleted);
	    
	    @Query("""
	    	    SELECT l.leaveType, SUM(l.totalDays)
	    	    FROM Leave l
	    	    WHERE l.employee.id = :employeeId
	    	      AND l.isDeleted = false
	    	      AND l.status = com.example.dev.model.LeaveStatus.APPROVED
	    	    GROUP BY l.leaveType
	    	""")
	    List<Object[]> getLeaveCountByEmployee(@Param("employeeId") String employeeId);
	    
	    
	    Page<Leave> findByIsDeletedFalse(Pageable pageable);

	    @Query("""
	           SELECT l FROM Leave l
	           WHERE l.isDeleted = false AND
	           (
	               LOWER(l.reason) LIKE LOWER(CONCAT('%', :search, '%'))
	               OR LOWER(l.employee.name) LIKE LOWER(CONCAT('%', :search, '%'))
	               OR LOWER(l.status) LIKE LOWER(CONCAT('%', :search, '%'))
	               OR LOWER(l.leaveType) LIKE LOWER(CONCAT('%', :search, '%'))
	           )
	           """)
	    Page<Leave> searchLeaves(@Param("search") String search,
	                             Pageable pageable);
	    
	    @Query("""
	    	       SELECT l.status, COUNT(l)
	    	       FROM Leave l
	    	       WHERE l.isDeleted = false
	    	       GROUP BY l.status
	    	       """)
	    	List<Object[]> countLeaveByStatus();

	    	Long countByIsDeletedFalse();
}

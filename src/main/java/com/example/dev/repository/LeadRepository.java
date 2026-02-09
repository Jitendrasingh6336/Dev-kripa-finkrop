package com.example.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev.model.Lead;
import com.example.dev.model.LeadStatus;
import com.example.dev.request.LoanTypeCountProjection;

public interface LeadRepository extends JpaRepository<Lead, String>{

	@Query("""
		    SELECT l FROM Lead l 
		    WHERE l.isDeleted = false
		      AND (
		           :search IS NULL OR 
		           LOWER(l.customerName) LIKE LOWER(CONCAT('%', :search, '%')) OR
		           LOWER(l.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
		           l.customerContactNumber LIKE CONCAT('%', :search, '%')
		      )
		""")
		Page<Lead> findLeads(@Param("search") String search, Pageable pageable);


	@Query("SELECT l FROM Lead l " +
		       "WHERE l.status = :status " +
		       "AND l.isDeleted = false " +
		       "AND (COALESCE(:search, '') = '' " +
		       "OR LOWER(l.customerName) LIKE LOWER(CONCAT('%', :search, '%')) " +
		       "OR LOWER(l.email) LIKE LOWER(CONCAT('%', :search, '%')))")
		Page<Lead> findLeadsByStatusAndSearch(
		        @Param("status") LeadStatus status,
		        @Param("search") String search,
		        Pageable pageable);


	boolean existsByEmailAndIsDeletedFalse(String email);

	boolean existsByCustomerContactNumberAndIsDeletedFalse(String customerContactNumber);
	
	boolean existsByPanCardNumberAndIsDeletedFalse(String panCardNumber);

	Optional<Lead> findByLeadIdAndIsDeleted(String id, Boolean false1);

	@Query("SELECT COUNT(l) FROM Lead l WHERE l.isDeleted = false OR l.isDeleted IS NULL")
	long countTotalLeads();


    @Query("SELECT COUNT(l) FROM Lead l WHERE l.status = 'APPROVED' AND l.isDeleted = false")
    long countApproved();

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.status = 'REJECTED' AND l.isDeleted = false")
    long countRejected();

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.status = 'UNDER_REVIEW' AND l.isDeleted = false")
    long countUnderReview();
    
    @Query("SELECT COUNT(l) FROM Lead l WHERE l.status = 'NEW' AND l.isDeleted = false")
    long countNewLeads();
    
    @Query("SELECT COUNT(l) FROM Lead l WHERE l.status = 'FOLLOW_UP' AND l.isDeleted = false")
    long countFollowUpLeads();
    
    @Query("""
    		   SELECT l FROM Lead l
    		   WHERE l.isDeleted = false
    		   ORDER BY l.createdDate DESC
    		""")
    		List<Lead> findRecentLeads();

    @Query("""
    	    SELECT l.serviceType AS loanType, COUNT(l) AS count
    	    FROM Lead l
    	    WHERE l.isDeleted = false
    	    GROUP BY l.serviceType
    	    ORDER BY COUNT(l) DESC
    	""")
    	List<LoanTypeCountProjection> countLeadsByLoanType();

    @Query("SELECT l FROM Lead l WHERE l.leadId = :leadId AND l.isDeleted = false")
    Optional<Lead> findActiveLead(@Param("leadId") String leadId);



}

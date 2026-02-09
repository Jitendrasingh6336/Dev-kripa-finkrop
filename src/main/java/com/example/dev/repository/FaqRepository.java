package com.example.dev.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev.model.Faq;
import com.example.dev.model.Lead;

public interface FaqRepository extends JpaRepository<Faq, String>{

	@Query("""
		    SELECT f FROM Faq f
		    WHERE f.isDeleted = false
		      AND (
		           :search IS NULL OR
		           LOWER(f.question) LIKE LOWER(CONCAT('%', :search, '%')) OR
		           LOWER(f.answer) LIKE LOWER(CONCAT('%', :search, '%')) OR
		           LOWER(f.category) LIKE LOWER(CONCAT('%', :search, '%'))
		      )
		""")
		Page<Faq> findFaqs(
		        @Param("search") String search,
		        Pageable pageable
		);

	Optional<Faq> findByFaqIdAndIsDeleted(String id, Boolean false1);
	
	Optional<Faq> findByQuestionAndIsDeleted(String question, Boolean isDeleted);

	Optional<Faq> findByAnswerAndIsDeleted(String answer, Boolean isDeleted);

	Optional<Faq> findByQuestionAndIsDeletedAndFaqIdNot(
	        String question,
	        Boolean isDeleted,
	        String faqId
	);

	Optional<Faq> findByAnswerAndIsDeletedAndFaqIdNot(
	        String answer,
	        Boolean isDeleted,
	        String faqId
	);



}

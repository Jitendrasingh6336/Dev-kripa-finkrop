package com.example.dev.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class CareerApplication {

	    @Id
	    @GeneratedValue(strategy = GenerationType.UUID)
	    private String id;

	    @Column(nullable = false)
	    private String name;

	    @Column(nullable = false)
	    private String email;

	    @Column(nullable = false, length = 15)
	    private String phone;

	 
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private Position position;

	    
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private Experience experience;

	    
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private Qualification qualification;

	   
	    @Column(length = 2000)
	    private String message;

	    // 🔹 Resume File Path / URL
	    private String resumeUrl;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private CareerStatus status;
	    
	    @CreatedDate
		@Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
		private LocalDateTime createdDate;

		@LastModifiedDate
		@Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
		private LocalDateTime updatedDate;

		@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
		private Boolean isDeleted = Boolean.FALSE;

		@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
		private Boolean isActive = Boolean.TRUE;
}

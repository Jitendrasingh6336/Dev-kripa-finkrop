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
@Builder
@Entity
public class CustomerQuery {
	
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;
    
    private String password;
    
    private String email;
    
    private String message;
    
    @Enumerated(EnumType.STRING)
    private LoanType serviceType;
    
    @Enumerated(EnumType.STRING)
    private QueryStatus queryStatus;
    
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

package com.example.dev.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.example.dev.util.ValidationConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

	@NotBlank(message = ValidationConstants.USER_NAME_REQUIRED)
    private String username;
	
	@NotBlank(message = ValidationConstants.PASSWORD_REQUIRED)
    private String password;
    
	@NotBlank(message = ValidationConstants.EMAIL_REQUIRED)
	@Email(message = "Invalid email format")
    private String email;
    
    @Enumerated(EnumType.STRING) 
    @NotNull(message = "Role is required")
    private Role role; 
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserStatus status = UserStatus.ACTIVE;
    
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

package com.example.dev.request;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.example.dev.model.LoanType;
import com.example.dev.model.QueryStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QueryRequest {

	@NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Service type is required")
    private LoanType serviceType;
    
    private QueryStatus queryStatus;
    
	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private Boolean isDeleted = Boolean.FALSE;

	private Boolean isActive = Boolean.TRUE;
}

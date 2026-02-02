package com.example.dev.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "leads") 
public class Lead {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String leadId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Lead status is required")
    private LeadStatus status;

    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String customerContactNumber;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN card number")
    private String panCardNumber;
    
    @Min(value = 300, message = "Civil score must be at least 300")
    @Max(value = 900, message = "Civil score cannot exceed 900")
    private Integer civilScore;
    
    private String companyName;
    
    @NotNull(message = "Loan amount is required")
    @Positive(message = "Loan amount must be greater than 0")
    private Double loanAmount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Employment type is required")
    private EmployeementType employmentType;

    private Double inHandSalary;
    
    private Boolean pfDeduction;
    
    @NotBlank(message = "Pin code is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid pin code")
    private String pinCode;
    
    private Double currentEmiAmount;
    
    private Integer companyExperience;

     @Enumerated(EnumType.STRING)
     @NotNull(message = "Service type is required")
    private LoanType serviceType;

    private String remarks;

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

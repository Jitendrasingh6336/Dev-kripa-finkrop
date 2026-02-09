package com.example.dev.response;

import com.example.dev.model.EmployeementType;
import com.example.dev.model.LeadSource;
import com.example.dev.model.LeadStatus;
import com.example.dev.model.LoanType;
import com.example.dev.model.MaritalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
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
public class LeadResponse {    
	  
	    private LeadStatus status;     

	    private String customerName;
	    
	    private String customerContactNumber;
	    
	    private String email;

	    private String panCardNumber;
	   
	    private Integer civilScore;

	    private String companyName;

	    private Double loanAmount;

	    private EmployeementType employmentType;

	    private MaritalStatus maritalStatus;

	    private String spouseName;  
	    
	    private String motherName;  

	    private LoanType serviceType;

	    private String remarks;

}

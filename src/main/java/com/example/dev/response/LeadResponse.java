package com.example.dev.response;

import com.example.dev.model.EmployeementType;
import com.example.dev.model.LeadSource;
import com.example.dev.model.LeadStatus;
import com.example.dev.model.LoanType;

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

	    private Double inHandSalary;

	    private Boolean pfDeduction;

	    private String pinCode;

	    private Double currentEmiAmount;

	    private Integer companyExperience;

	    private LoanType serviceType;
	   
	    private String leadStatus;

	    private String remarks;

}

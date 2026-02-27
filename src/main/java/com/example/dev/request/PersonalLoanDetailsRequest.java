package com.example.dev.request;

import com.example.dev.model.Lead;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class PersonalLoanDetailsRequest {
	
	    private String companyName;

	    private String companyAddress;

	    private String streetAddress;

	    private String city;

	    private String zipCode;

	    private String designation;

	    private String officialEmailId;

	    private Integer currentWorkExperience;

	    private Integer totalWorkExperience;

	    private Double inHandSalary;

	    private Boolean pfDeduction;

	    private Double currentEmiAmount;

	    private Lead lead;

}

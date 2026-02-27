package com.example.dev.request;

import com.example.dev.model.Lead;
import lombok.Data;

@Data
public class CarLoanDetailsRequest {

	
	    private String companyName;

	    private String companyAddress;

	    private String streetAddress;

	    private String city;

	    private String zipCode;

	    private String designation;

	    private String officialEmailId;

	    private Integer currentWorkExperience;

	    private Integer totalWorkExperience;

	    private Double monthlyInHandSalary;

	    private Boolean pfDeduction;

	    private Lead lead;
}

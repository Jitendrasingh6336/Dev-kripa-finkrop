package com.example.dev.request;

import com.example.dev.model.BusinessRole;
import com.example.dev.model.BusinessVintage;
import com.example.dev.model.Lead;
import lombok.Data;

@Data
public class BusinessLoanDetailsRequest {

	 
	    private String businessName;

	    private String businessAddress;

	    private String streetAddress;

	    private String city;

	    private String zipCode;

	    private BusinessRole role;

	    private BusinessVintage businessVintage;

	    private Double monthlyIncome;

	    private Boolean gstRegistered;

	    private Boolean itrFiled;

	    private Lead lead;
}

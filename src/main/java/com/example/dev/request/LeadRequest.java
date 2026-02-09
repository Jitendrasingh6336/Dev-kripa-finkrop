 package com.example.dev.request;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.example.dev.model.Address;
import com.example.dev.model.BusinessLoanDetails;
import com.example.dev.model.CarLoanDetails;
import com.example.dev.model.CreditCardLoanDetails;
import com.example.dev.model.EmployeementType;
import com.example.dev.model.InstantLoanDetails;
import com.example.dev.model.LeadSource;
import com.example.dev.model.LeadStatus;
import com.example.dev.model.LoanType;
import com.example.dev.model.MaritalStatus;
import com.example.dev.model.PersonalLoanDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LeadRequest {

	   
	    private LeadStatus status;

	    private String customerName;
	    
	    private String customerContactNumber;
	    
	    private String email;
	    
	    private String panCardNumber;
	   
	    private Double loanAmount;

	    private MaritalStatus maritalStatus;

	    private String spouseName;  
	    
	    private String motherName;  

	    private LoanType serviceType;

	    private String remarks;
		
	    private Address address;

	    private PersonalLoanDetails personalLoanDetails;

	    private BusinessLoanDetails businessLoanDetails;
	    
	    private InstantLoanDetails instantLoanDetails;
	    
	    private CarLoanDetails carLoanDetails;
	    
	    private CreditCardLoanDetails creditCardLoanDetails;
	    
	    private EmployeementType employmentType;

        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;

        private Boolean isDeleted = Boolean.FALSE;
        private Boolean isActive = Boolean.TRUE;


}

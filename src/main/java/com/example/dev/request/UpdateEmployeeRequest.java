package com.example.dev.request;

import java.time.LocalDate;

import com.example.dev.model.Department;
import com.example.dev.model.EmployeeStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class UpdateEmployeeRequest {

	
	    private String id;
	    
	    private String profilePhoto; 

	    private String firstName;

	    private String lastName;

	    private String email;

	    private String phone;
	    
	    private String username;
	    
	    private String password;

	    private LocalDate dateOfBirth;

	    private String address;

	    private String designation;

	    private LocalDate joiningDate;

	    private Double salary;

	    private EmployeeStatus status;

	    private Department department;
}

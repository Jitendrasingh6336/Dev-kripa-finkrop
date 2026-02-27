package com.example.dev.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.dev.model.Department;
import com.example.dev.model.EmployeeStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponse {

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
   
	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private Boolean isDeleted = Boolean.FALSE;

	private Boolean isActive = Boolean.TRUE;
}

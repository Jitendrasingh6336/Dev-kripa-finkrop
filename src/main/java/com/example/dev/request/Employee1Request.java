package com.example.dev.request;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import com.example.dev.model.EmployeeStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Employee1Request {

	@NotNull(message = "Profile image is required")
	private MultipartFile profile;
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotBlank(message = "First name is required")
	private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits" )
    private String phone;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Designation is required")
    private String designation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @NotNull(message = "Employee status is required")
    private EmployeeStatus employeeStatus;

    @NotBlank(message = "Department ID is required")
    private String departmentId;

    
}

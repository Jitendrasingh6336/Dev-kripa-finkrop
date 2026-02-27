package com.example.dev.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;
import com.example.dev.model.EmployeeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeRequest {

	
	@NotNull(message = "profilePhoto is required")
    private MultipartFile profilePhoto;

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "phone is required")
    private String phone;

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    @NotNull(message = "dateOfBirth is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "designation is required")
    private String designation;

    @NotNull(message = "joiningDate is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate joiningDate;

    @NotNull(message = "salary is required")
    private Double salary;

    @NotNull(message = "status is required")
    private EmployeeStatus status;

    @NotBlank(message = "departmentId is required")
    private String departmentId;
   
	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private Boolean isDeleted = Boolean.FALSE;

	private Boolean isActive = Boolean.TRUE;

}

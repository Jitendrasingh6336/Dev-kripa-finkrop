package com.example.dev.request;

import com.example.dev.util.ValidationConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	
	 
	@NotBlank(message = ValidationConstants.PASSWORD_REQUIRED)
    private String password;
    
	@NotBlank(message = ValidationConstants.EMAIL_REQUIRED)
	@Email(message = "Invalid email format")
    private String email;

}

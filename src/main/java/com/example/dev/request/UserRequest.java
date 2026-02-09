package com.example.dev.request;

import com.example.dev.model.Role;
import com.example.dev.model.UserStatus;
import com.example.dev.util.ValidationConstants;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {

	    
	    @NotBlank(message = ValidationConstants.USER_NAME_REQUIRED)
	    private String username;
		
		@NotBlank(message = ValidationConstants.PASSWORD_REQUIRED)
	    private String password;
	    
		@NotBlank(message = ValidationConstants.EMAIL_REQUIRED)
		@Email(message = "Invalid email format")
	    private String email;
	    
	    @NotNull(message = "Role is required")
	    private Role role;
	    
	    private UserStatus status;
}

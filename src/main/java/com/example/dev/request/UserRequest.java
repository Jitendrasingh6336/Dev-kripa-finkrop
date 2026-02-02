package com.example.dev.request;

import com.example.dev.model.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserRequest {

	    private String username;
	    private String password;
	    private String email;
	    private Role role; 
}

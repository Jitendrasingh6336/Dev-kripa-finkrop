package com.example.dev.response;

import com.example.dev.model.Role;
import com.example.dev.model.UserStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

	private String id;
    private String username;
    private String email;
    private Role role;
    private UserStatus status; 
}

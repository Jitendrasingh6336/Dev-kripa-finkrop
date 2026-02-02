package com.example.dev.service;

import com.example.dev.request.UserRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.UserResponse;

public interface IUserService {
	
	public ApiResponse addUser(UserRequest userRequest);
	
	UserResponse getUserByUsername(String username);

}

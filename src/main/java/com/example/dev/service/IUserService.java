package com.example.dev.service;

import com.example.dev.model.ChangePasswordRequest;
import com.example.dev.request.UserRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.UserResponse;

public interface IUserService {
	
	public ApiResponse addUser(UserRequest userRequest);
	
	UserResponse getUserByUsername(String username);
	
	public ApiResponse getAllUsers(Integer pageNumber, Integer pageSize, String search);

	public ApiResponse deleteUser(String id);
	
	public ApiResponse getUserCounts() ;
	
	public ApiResponse changeUserStatus(String id);
	
	public ApiResponse changePassword(String email, ChangePasswordRequest request);
	

}

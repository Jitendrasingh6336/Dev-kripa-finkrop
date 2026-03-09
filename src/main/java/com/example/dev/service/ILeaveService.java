package com.example.dev.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.dev.request.LeaveRequest;
import com.example.dev.response.ApiResponse;

public interface ILeaveService {

	
	public ApiResponse addLeave(LeaveRequest leaveRequest);
	
	public ApiResponse getAllLeave(Integer pageNumber, Integer pageSize, String search);
	
	public ApiResponse deleteLeave(String id);
	
	public ApiResponse getLeaveBalance(String employeeId) ;
	
	public ApiResponse getLeaveStatusCount();
	
	
	
}

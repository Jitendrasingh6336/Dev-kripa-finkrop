package com.example.dev.service;

import com.example.dev.request.AttendanceRequest;
import com.example.dev.response.ApiResponse;

public interface IAttendanceService {

	public ApiResponse addAttendance(AttendanceRequest attendanceRequest);
	
	public ApiResponse clockOut(String employeeId);
}

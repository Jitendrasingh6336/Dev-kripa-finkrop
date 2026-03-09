package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.request.AttendanceRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.IAttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/attendance/api/v1")
@Validated
public class AttendanceController {
	
	@Autowired
	private IAttendanceService attendanceService;

	
	 @PostMapping ("/web/add")
	 public ResponseEntity<ApiResponse> addAttendance(@Valid @RequestBody AttendanceRequest request){

		 return ResponseEntity.status(HttpStatus.OK).body(this.attendanceService.addAttendance(request));
	 }
}

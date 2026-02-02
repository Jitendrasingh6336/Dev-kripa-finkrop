package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.request.LeadRequest;
import com.example.dev.request.UserRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/api/v1/add")
	public ResponseEntity<ApiResponse> addUser(@RequestBody UserRequest request) {
		
		return ResponseEntity.status(HttpStatus.OK).body(this.userService.addUser(request));
	}

}

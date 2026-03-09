package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dev.request.LeaveRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.ILeaveService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/leave/api/v1")
@CrossOrigin("*")
@Validated
public class LeaveController {

	@Autowired
	private ILeaveService leaveService;
	
	@PostMapping("/web/add")
    public ResponseEntity<ApiResponse> addLeave(@Valid @RequestBody LeaveRequest leaveRequest) {
        ApiResponse response = leaveService.addLeave(leaveRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
	
	@GetMapping("/web/get-leave-balance")
	public ResponseEntity<ApiResponse> getLeaveBalance(@Valid @RequestParam("employeeId") @NotBlank(message = "employeeId is required") String employeeId){
		return ResponseEntity.ok(this.leaveService.getLeaveBalance(employeeId));
	}
	
	 @GetMapping("/web/get-all-leaves")
	 public ResponseEntity<ApiResponse> getAllLeaves(
	            @RequestParam(defaultValue = "0") Integer pageNumber,
	            @RequestParam(defaultValue = "10") Integer pageSize,
	            @RequestParam(required = false) String search) {

	        ApiResponse response = leaveService.getAllLeave(pageNumber, pageSize, search);

	        return ResponseEntity.ok(response);
	  }
	 
	 @DeleteMapping("/web/delete-leave")
	 public ResponseEntity<ApiResponse> deleteLeave(@Valid @NotBlank(message = "id is required") @RequestParam String id) {

	        ApiResponse response = leaveService.deleteLeave(id);
	        return ResponseEntity.ok(response);
	    }
	 
	 @GetMapping("/web/status-count")
	 public ResponseEntity<ApiResponse> getLeaveStatusCount() {
	     return ResponseEntity.ok(leaveService.getLeaveStatusCount());
	 }
	 
	 
}

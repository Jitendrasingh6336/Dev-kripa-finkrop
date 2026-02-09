package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dev.request.UserRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.IUserService;
import com.example.dev.util.Constants;
import com.example.dev.util.ValidationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Validated
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/api/v1/add")
	public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserRequest request) {
		
		return ResponseEntity.status(HttpStatus.OK).body(this.userService.addUser(request));
	}
	
	@GetMapping("/api/v1/count")
    public ResponseEntity<ApiResponse> getUsersCount() {
        return ResponseEntity.ok(userService.getUserCounts());
    }
	

	 @DeleteMapping("/api/v1/delete")
	    public ResponseEntity<ApiResponse> deleteUser(@NotBlank(message = "id is required") @RequestParam String id) {
	        return ResponseEntity.ok(userService.deleteUser(id));
	 }
	 
	 
	 @GetMapping("/api/v1/get-all-users")
	    public ResponseEntity<ApiResponse> getUsers(
	    		   @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
	               @RequestParam(defaultValue = Constants.DEFAULT_PAGE_LIMIT, required = false) Integer pageSize,
	               @RequestParam(defaultValue = "", required = false) String search) {
	     
		 return ResponseEntity.ok(this.userService.getAllUsers(pageNumber, pageSize, search));
	 }
	 
	 @PutMapping("/api/v1/status-change")
	 public ResponseEntity<ApiResponse> changeStatus(@Valid @RequestParam @NotBlank(message = ValidationConstants.USER_ID_REQUIRED) String id){
			return ResponseEntity.ok(this.userService.changeUserStatus(id));
		}
	 
}

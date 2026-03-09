package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dev.request.Employee1Request;
import com.example.dev.request.EmployeeRequest;
import com.example.dev.request.UpdateEmployeeRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.IEmployeeService;
import com.example.dev.util.Constants;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/employee/api/v1")
@CrossOrigin("*")
@Validated
public class EmployeeController {

	
	@Autowired
	private IEmployeeService employeeService;
	
	
	@PostMapping("/web/add1")
		public ResponseEntity<ApiResponse> addEmp(@Valid Employee1Request request) {
		    System.err.println("employee: " + request.getProfile());
		    System.err.println(request);
		    return ResponseEntity
		            .status(HttpStatus.CREATED)
		            .body(employeeService.addEmp(request));
	}
//	@PostMapping(value = "/web/add1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<ApiResponse> addEmp(
//	        @ModelAttribute @Valid Employee1Request request
//	) {
//	    System.err.println("employee: " + request.getProfile());
//	    System.err.println(request);
//
//	    return ResponseEntity
//	            .status(HttpStatus.CREATED)
//	            .body(employeeService.addEmp(request));
//	}
	
	//get employee
	@GetMapping("/web/get-employees")
    public ResponseEntity<ApiResponse> getEmployees(
    		   @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
               @RequestParam(defaultValue = Constants.DEFAULT_PAGE_LIMIT, required = false) Integer pageSize,
               @RequestParam(defaultValue = "", required = false) String search) {
        return ResponseEntity.ok(this.employeeService.getAllEmployee(pageNumber, pageSize, search));
    }
	
	//get employee by id
	@GetMapping("/web/get-employee")
	public ResponseEntity<ApiResponse> getEmployee(@Valid @RequestParam("id") @NotBlank(message = "id is required") String id){
		return ResponseEntity.ok(this.employeeService.getEmployeeById(id));
	}
	
	// ✅ Update Employee
    @PutMapping("/web/update")
    public ResponseEntity<ApiResponse> updateEmployee(@RequestBody UpdateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(request));
    }
    
    @DeleteMapping("/web/delete")
    public ResponseEntity<ApiResponse> deleteEmployee(@Valid @RequestParam  @NotBlank(message = "Employee id is required") String id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }
    
    @GetMapping("/web/names")
    public ResponseEntity<ApiResponse> getAllEmployeeNames() {
        return ResponseEntity.ok(employeeService.getAllEmployeeNames());
    }
}

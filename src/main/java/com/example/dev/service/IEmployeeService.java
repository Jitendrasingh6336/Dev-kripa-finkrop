package com.example.dev.service;
import com.example.dev.request.EmployeeRequest;
import com.example.dev.request.UpdateEmployeeRequest;
import com.example.dev.response.ApiResponse;

public interface IEmployeeService {

	public ApiResponse addEmployee(EmployeeRequest employeeRequest);

	public ApiResponse getAllEmployee(Integer pageNumber, Integer pageSize, String search);
	
	public ApiResponse updateEmployee(UpdateEmployeeRequest updateEmployeeRequest);
	
	public ApiResponse deleteEmployee(String id);
	
	public ApiResponse getEmployeeById(String id);
	
	public ApiResponse getAllEmployeeByName();
	
	public ApiResponse getAllEmployeeNames();
}

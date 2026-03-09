package com.example.dev.service;
import com.example.dev.request.Employee1Request;
import com.example.dev.request.EmployeeRequest;
import com.example.dev.request.UpdateEmployeeRequest;
import com.example.dev.response.ApiResponse;

public interface IEmployeeService {

	public ApiResponse getAllEmployee(Integer pageNumber, Integer pageSize, String search);
	
	public ApiResponse updateEmployee(UpdateEmployeeRequest updateEmployeeRequest);
	
	public ApiResponse deleteEmployee(String id);
	
	public ApiResponse getEmployeeById(String id);
	
	public ApiResponse getAllEmployeeByName();
	
	public ApiResponse getAllEmployeeNames();
	
	public ApiResponse addEmp(Employee1Request employee1Request);
}

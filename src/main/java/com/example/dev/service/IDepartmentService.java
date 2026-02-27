package com.example.dev.service;

import com.example.dev.request.DepartmentRequest;
import com.example.dev.request.UpdateDepartmentRequest;
import com.example.dev.response.ApiResponse;

public interface IDepartmentService {

	    ApiResponse addDepartment(DepartmentRequest request);

	    ApiResponse updateDepartment( UpdateDepartmentRequest request);

	    ApiResponse deleteDepartment(String id);

	    ApiResponse getAllDepartment(Integer pageNumber, Integer pageSize, String search);

	    ApiResponse getDepartmentById(String id);
}

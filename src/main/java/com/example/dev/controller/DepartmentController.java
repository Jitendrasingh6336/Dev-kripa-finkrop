package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.request.DepartmentRequest;
import com.example.dev.request.UpdateDepartmentRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.IDepartmentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/department/api/v1")
@CrossOrigin("*")
@Validated
public class DepartmentController {
	
	@Autowired
	private IDepartmentService departmentService;
	
	 // Add Department
    @PostMapping("/web/add")
    public ApiResponse addDepartment(@Valid @RequestBody DepartmentRequest request) {
        return departmentService.addDepartment(request);
    }


    // Update Department
    @PutMapping("/web/update")
    public ApiResponse updateDepartment(@Valid @RequestBody UpdateDepartmentRequest request) {
        return departmentService.updateDepartment(request);
    }


    // Delete Department
    @DeleteMapping("/web/delete")
    public ApiResponse deleteDepartment(@Valid @NotBlank(message = "id is required") @RequestParam String id) {
        return departmentService.deleteDepartment(id);
    }


    // Get All Departments with pagination and search
    @GetMapping("/web/getAll")
    public ApiResponse getAllDepartment(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String search) {

        return departmentService.getAllDepartment(pageNumber, pageSize, search);
    }


    // Get Department By Id
    @GetMapping("/getById/{id}")
    public ApiResponse getDepartmentById(@PathVariable String id) {
        return departmentService.getDepartmentById(id);
    }


	
}

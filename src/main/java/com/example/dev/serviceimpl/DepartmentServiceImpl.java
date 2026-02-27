package com.example.dev.serviceimpl;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dev.exception.ResourceAlreadyExistException;
import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.model.Department;
import com.example.dev.model.Faq;
import com.example.dev.repository.DepartmentRepository;
import com.example.dev.request.DepartmentRequest;
import com.example.dev.request.UpdateDepartmentRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.DepartmentResponse;
import com.example.dev.response.FaqResponse;
import com.example.dev.response.PaginatedResponse;
import com.example.dev.service.IDepartmentService;
import com.example.dev.util.AppUtil;
import com.example.dev.util.Constants;

@Service
public class DepartmentServiceImpl implements IDepartmentService{

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Override
	public ApiResponse addDepartment(DepartmentRequest request) {
		// TODO Auto-generated method stub
		
		Optional<Department> departmentOpt = departmentRepository
		        .findByNameIgnoreCaseAndIsDeletedFalse(request.getName());

		if (departmentOpt.isPresent()) {
		    throw new ResourceAlreadyExistException("Department already exists");
		}
		
		 Department department = Department.builder().name(request.getName()).description(request.getDescription())
				    .isActive(Boolean.TRUE).createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).isDeleted(Boolean.FALSE)
		            .build();
		 Department savedDepartment =  departmentRepository.save(department);

		    return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
					.message(Constants.DEPARTMENT_CREATED_SUCCESSFULLY).response(savedDepartment).build();
	}

	@Override
	public ApiResponse updateDepartment(UpdateDepartmentRequest request) {
		// TODO Auto-generated method stub
		Department department = departmentRepository.findByIdAndIsDeleted(request.getId(),Boolean.FALSE)
	            .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getId()));
		
	    // Update Fields
	    department.setName(request.getName());
	    department.setDescription(request.getDescription());
	    department.setUpdatedDate(LocalDateTime.now());

	    Department updateDepartment = departmentRepository.save(department);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Department updated successfully")
	            .response(updateDepartment)
	            .build();
	}

	@Override
	public ApiResponse deleteDepartment(String id) {
		// TODO Auto-generated method stub
		Department department = this.departmentRepository.findByIdAndIsDeleted(id, Boolean.FALSE)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
		department.setIsActive(Boolean.FALSE);
		department.setIsDeleted(Boolean.TRUE);
		this.departmentRepository.save(department);
		return ApiResponse.builder().message(Constants.DEPARTMENT_DELETE_SUCCESS).statusCode(HttpStatus.OK.value())
				.build();
	}

	@Override
	public ApiResponse getAllDepartment(Integer pageNumber, Integer pageSize, String search) {
		// TODO Auto-generated method stub
		 Pageable pageable = AppUtil.getPageable(pageNumber, pageSize);
		    Page<Department> departmentPage;

		    if (search != null && !search.trim().isEmpty()) {
		        departmentPage = departmentRepository.searchDepartments(search.trim(), pageable);
		    } else {
		        departmentPage = departmentRepository.findByIsDeletedFalse(pageable);
		    }

		    Page<DepartmentResponse> responsePage =
		            departmentPage.map(this::departmentToDepartmentResponse);

		    return ApiResponse.builder()
		            .statusCode(HttpStatus.OK.value())
		            .message(Constants.DEPARTMENT_FETCHED_SUCCESSFULLY)
		            .response(new PaginatedResponse<>(responsePage))
		            .build();
	}

	@Override
	public ApiResponse getDepartmentById(String id) {
		// TODO Auto-generated method stub
		Department department = departmentRepository
	            .findByIdAndIsDeleted(id,Boolean.FALSE)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Department not found with id: " + id));

	    DepartmentResponse response = departmentToDepartmentResponse(department);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message(Constants.DEPARTMENT_FETCHED_SUCCESSFULLY)
	            .response(response)
	            .build();
	}

	private DepartmentResponse departmentToDepartmentResponse(Department department) {

	    return DepartmentResponse.builder()
	            .id(department.getId())
	            .name(department.getName())
	            .description(department.getDescription())
	            
	            .build();
	}
}

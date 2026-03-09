package com.example.dev.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.dev.exception.ResourceAlreadyExistException;
import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.model.Department;
import com.example.dev.model.Employee1;
import com.example.dev.repository.DepartmentRepository;
import com.example.dev.repository.Employee1Repository;

import com.example.dev.request.Employee1Request;
import com.example.dev.request.EmployeeRequest;
import com.example.dev.request.UpdateEmployeeRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.EmployeeNameResponse;
import com.example.dev.response.EmployeeResponse;
import com.example.dev.response.PaginatedResponse;
import com.example.dev.service.IEmployeeService;
import com.example.dev.util.AppUtil;
import com.example.dev.util.Constants;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private Employee1Repository employeeRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private Employee1Repository employee1Repository;
	
	@Autowired
	private AppUtil appUtil;
	
	

	@Override
	public ApiResponse getAllEmployee(Integer pageNumber, Integer pageSize, String search) {
		// TODO Auto-generated method stub
		
		 Pageable pageable = AppUtil.getPageable(pageNumber, pageSize);
		    Page<Employee1> employeePage;

		    if (search != null && !search.trim().isEmpty()) {
		        employeePage = employeeRepository.searchEmployees(search.trim(), pageable);
		    } else {
		        employeePage = employeeRepository.findByIsDeletedFalse(pageable);
		    }

		    // Convert to DTO
		    Page<EmployeeResponse> responsePage =
		            employeePage.map(this::employeeToEmployeeResponse);

		    return ApiResponse.builder()
		            .statusCode(HttpStatus.OK.value())
		            .message(Constants.EMPLOYEE_FETCHED_SUCCESSFULLY)
		            .response(new PaginatedResponse<>(responsePage))
		            .build();
		
	}

	@Override
	public ApiResponse updateEmployee(UpdateEmployeeRequest updateEmployeeRequest) {
		// TODO Auto-generated method stub
		
		Employee1 employee = employeeRepository.findById(updateEmployeeRequest.getId())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Employee not found with id: " + updateEmployeeRequest.getId()));

	    
	    Optional<Employee1> existingEmailEmployee = employeeRepository
	            .findByEmailIgnoreCase(updateEmployeeRequest.getEmail());

	    if (existingEmailEmployee.isPresent() &&
	            !existingEmailEmployee.get().getId().equals(updateEmployeeRequest.getId())) {
	        throw new ResourceAlreadyExistException("Employee already exists with this email");
	    }

	   
	    Optional<Employee1> existingUsernameEmployee = employeeRepository
	            .findByUsernameIgnoreCase(updateEmployeeRequest.getUsername());

	    if (existingUsernameEmployee.isPresent() &&
	            !existingUsernameEmployee.get().getId().equals(updateEmployeeRequest.getId())) {
	        throw new ResourceAlreadyExistException("Employee already exists with this username");
	    }

	    // Update fields
//	    employee.setProfilePhoto(updateEmployeeRequest.getProfilePhoto());
	    employee.setFirstName(updateEmployeeRequest.getFirstName());
	    employee.setLastName(updateEmployeeRequest.getLastName());
	    employee.setEmail(updateEmployeeRequest.getEmail());
	    employee.setPhone(updateEmployeeRequest.getPhone());
	    employee.setUsername(updateEmployeeRequest.getUsername());
	    employee.setDateOfBirth(updateEmployeeRequest.getDateOfBirth());
	    employee.setAddress(updateEmployeeRequest.getAddress());
	    employee.setDesignation(updateEmployeeRequest.getDesignation());
	    employee.setJoiningDate(updateEmployeeRequest.getJoiningDate());
	    employee.setDepartment(updateEmployeeRequest.getDepartment());

	    // Save updated employee
	    employeeRepository.save(employee);

	    return ApiResponse.builder().message(Constants.FAQ_DELETE_SUCCESS).statusCode(HttpStatus.OK.value())
	    		.response(employee)
				.build();
	}

	@Override
	public ApiResponse deleteEmployee(String id) {
		// TODO Auto-generated method stub
		Employee1 employee = this.employeeRepository.findByIdAndIsDeleted(id, Boolean.FALSE)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.FAQ_NOT_FOUND));
		employee.setIsActive(Boolean.FALSE);
		employee.setIsDeleted(Boolean.TRUE);
		this.employeeRepository.save(employee);
		return ApiResponse.builder().message(Constants.FAQ_DELETE_SUCCESS).statusCode(HttpStatus.OK.value())
				.build();
		
	}

	public EmployeeResponse employeeToEmployeeResponse(Employee1 employee) {

	    return EmployeeResponse.builder()
	    		.id(employee.getId())
	            .firstName(employee.getFirstName())
	            .lastName(employee.getLastName())
	            .email(employee.getEmail())
	            .phone(employee.getPhone())
	            .department(employee.getDepartment())
	            .address(employee.getAddress())
	            .isActive(employee.getIsActive())
	            .build();
	}

	@Override
	public ApiResponse getEmployeeById(String id) {
		// TODO Auto-generated method stub
		   
	    Employee1 employee = employeeRepository.findByIdAndIsDeleted(id, Boolean.FALSE)
	            .orElseThrow(() -> new ResourceNotFoundException(Constants.EMPLOYEE_NOT_FOUND));

	    EmployeeResponse employeeResponse = employeeToEmployeeResponse(employee);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message(Constants.EMPLOYEE_FETCHED_SUCCESSFULLY)
	            .response(employeeResponse)
	            .build();
	}

	@Override
	public ApiResponse getAllEmployeeByName() {
		// TODO Auto-generated method stub
	    List<Employee1> employeeList = employeeRepository.findByIsDeletedFalse();

	    List<EmployeeResponse> responseList = employeeList.stream()
	            .map(this::employeeToEmployeeResponse)
	            .toList();

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message(Constants.EMPLOYEE_FETCHED_SUCCESSFULLY)
	            .response(responseList)
	            .build();
	}

	@Override
	public ApiResponse getAllEmployeeNames() {
		// TODO Auto-generated method stub
		List<Employee1> employeeList = employeeRepository.findAllActiveEmployees();

	    List<EmployeeNameResponse> nameList = employeeList.stream()
	            .map(emp -> EmployeeNameResponse.builder().firstName(emp.getFirstName()).lastName(emp.getLastName()).build()).toList();

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Employee names fetched successfully")
	            .response(nameList)
	            .build();
	}

	@Override
	public ApiResponse addEmp(Employee1Request employee1Request) {

	    Department department = null;
	    if (employee1Request.getDepartmentId() != null) {
	        department = departmentRepository.findById(employee1Request.getDepartmentId())
	                .orElseThrow(() -> new RuntimeException("Department not found"));
	    }

	    Employee1 employee = Employee1.builder()
	            .name(employee1Request.getName()).firstName(employee1Request.getFirstName()).lastName(employee1Request.getLastName())
	            .address(employee1Request.getAddress()).designation(employee1Request.getDesignation())
	            .email(employee1Request.getEmail()).password(employee1Request.getPassword()).phone(employee1Request.getPhone())
	            .username(employee1Request.getUsername()).dateOfBirth(employee1Request.getDateOfBirth()) .joiningDate(employee1Request.getJoiningDate())
	            .employeeStatus(employee1Request.getEmployeeStatus()).isActive(Boolean.TRUE).isDeleted(Boolean.FALSE)
	            .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now())
	            .department(department)
	            .build();

	    // Upload profile
	    if (employee1Request.getProfile() != null) {
	        String fileName = appUtil.uploadPhoto(
	                employee1Request.getProfile(),
	                Constants.EMPLOYEE_APPLICATION_IMG
	        );
	        employee.setProfile(fileName);
	    }

	    Employee1 savedEmployee = employee1Repository.save(employee);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.CREATED.value())
	            .message("Employee created successfully")
	            .response(savedEmployee)
	            .build();
	}
}

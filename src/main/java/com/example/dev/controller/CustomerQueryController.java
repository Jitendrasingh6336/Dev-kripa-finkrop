package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.request.QueryRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.ICustomerQueryService;
import com.example.dev.util.Constants;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/customer-query")
@CrossOrigin("*")
@Validated
public class CustomerQueryController {
	
	private final ICustomerQueryService customerQueryService;

    public CustomerQueryController(ICustomerQueryService customerQueryService) {
        this.customerQueryService = customerQueryService;
    }


	 //  Create Customer Query
    @PostMapping("/api/v1/create")
    public ApiResponse createCustomerQuery(@Valid @RequestBody QueryRequest queryRequest) {
        return customerQueryService.createCustomerQuery(queryRequest);
    }

    // Update Query Status
    @PutMapping("/api/v1/update-status")
    public ApiResponse updateQueryStatus(@Valid @RequestParam
            @NotBlank(message = "Query id is required")
            String id,
            @Valid
            @RequestParam
            @NotBlank(message = "Status is required")
            String status) {
    	
        return customerQueryService.updateQueryStatus(id, status);
    }
    
    @GetMapping("/api/v1/get-customer-query")
    public ResponseEntity<ApiResponse> getLeads(
    		   @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
               @RequestParam(defaultValue = Constants.DEFAULT_PAGE_LIMIT, required = false) Integer pageSize,
               @RequestParam(defaultValue = "", required = false) String search) {
        return ResponseEntity.ok(this.customerQueryService.getAllCustomerQuerys(pageNumber, pageSize, search));
    }
    
    @GetMapping("/api/v1/customer-query-by-id")
    public ResponseEntity<ApiResponse> getCustomerQueryById(@Valid @RequestParam @NotBlank(message = "id is required") String id) {
        ApiResponse response = customerQueryService.getCustomerQueryById(id);
        return ResponseEntity.ok(response);
    }

}

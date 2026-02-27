package com.example.dev.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.model.CustomerQuery;
import com.example.dev.model.Faq;
import com.example.dev.model.QueryStatus;
import com.example.dev.repository.CustomerQueryRepository;
import com.example.dev.request.PaginationRequest;
import com.example.dev.request.QueryRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.CustomerQueryResponse;
import com.example.dev.response.PaginatedResponse;
import com.example.dev.service.ICustomerQueryService;
import com.example.dev.util.AppUtil;
import com.example.dev.util.Constants;

@Service
public class CustomerQueryServiceImpl implements ICustomerQueryService {

	@Autowired
	private CustomerQueryRepository customerQueryRepository;
	
	@Override
	public ApiResponse createCustomerQuery(QueryRequest queryRequest) {
		// TODO Auto-generated method stub
		 CustomerQuery query = CustomerQuery.builder()
				    .username(queryRequest.getUsername()).email(queryRequest.getEmail()).message(queryRequest.getMessage()).queryStatus(QueryStatus.NEW)
		            .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).isDeleted(Boolean.FALSE).isActive(Boolean.TRUE)
		            .serviceType(queryRequest.getServiceType())
		            .build();
		 CustomerQuery savedQuery = customerQueryRepository.save(query);

		    return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
					.message(Constants.CUSTOMER_QUERY_CREATED_SUCCESSFULLY).response(savedQuery).build();
	}

	@Override
	public ApiResponse updateQueryStatus(String id, String status) {
		// TODO Auto-generated method stub
		CustomerQuery query = customerQueryRepository.findByIdAndIsDeleted(id, Boolean.FALSE)
	            .orElseThrow(() -> new ResourceNotFoundException("Query not found"));

	    QueryStatus queryStatus;
	    try {
	        queryStatus = QueryStatus.valueOf(status.toUpperCase());
	    } catch (IllegalArgumentException ex) {
	        return ApiResponse.builder()
	                .statusCode(HttpStatus.BAD_REQUEST.value())
	                .message("Invalid query status. Allowed values: NEW, INPROGRESS, RESOLVED")
	                .build();
	    }

	    query.setQueryStatus(queryStatus);
	    query.setUpdatedDate(LocalDateTime.now());

	    customerQueryRepository.save(query);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Query status updated successfully")
	            .response(query)
	            .build();
	}

	@Override
	public ApiResponse getAllCustomerQuerys(Integer pageNumber, Integer pageSize, String search) {

		 // 🔹 Use Common Pagination Utility
	    Pageable pageable = AppUtil.getPageable(pageNumber, pageSize);

	    Page<CustomerQuery> queryPage;

	    if (search != null && !search.trim().isEmpty()) {
	        queryPage = customerQueryRepository
	                .searchCustomerQueries(search.trim(), pageable);
	    } else {
	        queryPage = customerQueryRepository
	                .findByIsDeletedFalse(pageable);
	    }

	    // 🔹 Convert to DTO
	    Page<CustomerQueryResponse> responsePage =
	            queryPage.map(this::customerQueryToResponse);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message(Constants.CUSTOMER_QUERY_FETCHED)
	            .response(new PaginatedResponse<>(responsePage))
	            .build();
	}

	private CustomerQueryResponse customerQueryToResponse(CustomerQuery query) {

	    return CustomerQueryResponse.builder()
	            .id(query.getId())
	            .username(query.getUsername())
	            .email(query.getEmail())
	            .message(query.getMessage())
	            .serviceType(query.getServiceType())
	            .queryStatus(query.getQueryStatus())
	            .build();
	}

	@Override
	public ApiResponse getCustomerQueryById(String id) {

	    CustomerQuery query = customerQueryRepository
	            .findByIdAndIsDeleted(id, Boolean.FALSE)
	            .orElseThrow(() -> new ResourceNotFoundException("Customer query not found"));

	    CustomerQueryResponse response = customerQueryToResponse(query);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message(Constants.CUSTOMER_QUERY_FETCHED)
	            .response(response)
	            .build();
	}
}

package com.example.dev.service;

import com.example.dev.request.LeadRequest;
import com.example.dev.request.QueryRequest;
import com.example.dev.response.ApiResponse;

public interface ICustomerQueryService {
	
	public ApiResponse createCustomerQuery(QueryRequest queryRequest);
	
	public ApiResponse updateQueryStatus(String id,String status);
	
	public ApiResponse getAllCustomerQuerys(Integer pageNumber, Integer pageSize, String search);
	
	public ApiResponse getCustomerQueryById(String id);


}

package com.example.dev.service;

import com.example.dev.model.LeadRemarksRequest;
import com.example.dev.request.LeadRequest;
import com.example.dev.request.UpdateLeadRequest;
import com.example.dev.response.ApiResponse;

public interface ILeadService {

	public ApiResponse createLead(LeadRequest leadRequest);

	public ApiResponse updateLead(UpdateLeadRequest updateLeadRequest);
	
	public ApiResponse updateLeadStatus(String status,String id);

	public ApiResponse getLeadById(String id);

	public ApiResponse getAllLeads(Integer pageNumber, Integer pageSize, String search);

	public ApiResponse deleteLead(String id);
	
	public ApiResponse getLeadsByStatus(String status,Integer pageNumber, Integer pageSize, String search);
	
	public ApiResponse getLeadCounts();
	
	public ApiResponse getRecentLeads();
	
	public ApiResponse getLoanTypeChartData();
	
	public ApiResponse addLeadRemarks(String leadId, LeadRemarksRequest request) ;


}

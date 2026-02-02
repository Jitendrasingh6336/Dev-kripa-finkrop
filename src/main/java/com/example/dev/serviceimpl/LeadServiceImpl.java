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
import com.example.dev.model.Lead;
import com.example.dev.model.LeadStatus;
import com.example.dev.repository.LeadRepository;
import com.example.dev.request.LeadRequest;
import com.example.dev.request.PaginationRequest;
import com.example.dev.request.UpdateLeadRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.LeadCountResponse;
import com.example.dev.response.LeadResponse;
import com.example.dev.response.PaginatedResponse;
import com.example.dev.service.ILeadService;
import com.example.dev.util.AppUtil;
import com.example.dev.util.Constants;

@Service
public class LeadServiceImpl implements ILeadService {

	
	@Autowired
	private LeadRepository leadRepository;
	
	@Autowired
	private AppUtil appUtil;
	
	@Override
	public ApiResponse createLead(LeadRequest leadRequest) {
		
		// ✅ Check Email Exists
	    if (leadRepository.existsByEmailAndIsDeletedFalse(leadRequest.getEmail())) {
	        throw new ResourceAlreadyExistException("Email already exists");
	    }

	    // ✅ Check Phone Number Exists
	    if (leadRepository.existsByCustomerContactNumberAndIsDeletedFalse(leadRequest.getCustomerContactNumber())) {
	        throw new ResourceAlreadyExistException("Contact number already exists");
	    }
	    
	    if (leadRepository.existsByPanCardNumberAndIsDeletedFalse(leadRequest.getPanCardNumber())) {
	        throw new ResourceAlreadyExistException("PAN card number already exists");
	    }
	    
	    // Create Lead
	    Lead lead = Lead.builder()
	            .status(leadRequest.getStatus())
	            .civilScore(leadRequest.getCivilScore())
	            .companyExperience(leadRequest.getCompanyExperience()).companyName(leadRequest.getCompanyName())
	            .customerContactNumber(leadRequest.getCustomerContactNumber()).currentEmiAmount(leadRequest.getCurrentEmiAmount())
	            .customerName(leadRequest.getCustomerName()).email(leadRequest.getEmail()).employmentType(leadRequest.getEmploymentType())
	            .inHandSalary(leadRequest.getInHandSalary()).loanAmount(leadRequest.getLoanAmount()).status(leadRequest.getStatus())
	            .serviceType(leadRequest.getServiceType()).pfDeduction(leadRequest.getPfDeduction()).pinCode(leadRequest.getPinCode())
	            .remarks(leadRequest.getRemarks()).panCardNumber(leadRequest.getPanCardNumber()).createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now())
	            .isDeleted(Boolean.FALSE).isActive(Boolean.TRUE)
	            .build();
	    Lead savedLead = leadRepository.save(lead);

	    return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
				.message(Constants.LEAD_CREATED_SUCCESSFULLY).response(savedLead).build();
	    }

	@Override
	public ApiResponse updateLead(UpdateLeadRequest updateLeadRequest) {
		// TODO Auto-generated method stub
		Lead lead = leadRepository.findByLeadIdAndIsDeleted(updateLeadRequest.getId(),Boolean.FALSE)
	            .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + updateLeadRequest.getId()));

	    // ✅ Check Email Exists (except current lead)
	    if (!lead.getEmail().equals(updateLeadRequest.getEmail())
	            && leadRepository.existsByEmailAndIsDeletedFalse(updateLeadRequest.getEmail())) {
	        throw new ResourceAlreadyExistException("Email already exists");
	    }

	    // ✅ Check Contact Number Exists (except current lead)
	    if (!lead.getCustomerContactNumber().equals(updateLeadRequest.getCustomerContactNumber())
	            && leadRepository.existsByCustomerContactNumberAndIsDeletedFalse(updateLeadRequest.getCustomerContactNumber())) {
	        throw new ResourceAlreadyExistException("Contact number already exists");
	    }

	    // ✅ Check PAN Exists (except current lead)
	    if (!lead.getPanCardNumber().equals(updateLeadRequest.getPanCardNumber())
	            && leadRepository.existsByPanCardNumberAndIsDeletedFalse(updateLeadRequest.getPanCardNumber())) {
	        throw new ResourceAlreadyExistException("PAN card number already exists");
	    }

	    // ✅ Update Fields
	    lead.setCustomerName(updateLeadRequest.getCustomerName());
	    lead.setEmail(updateLeadRequest.getEmail());
	    lead.setCustomerContactNumber(updateLeadRequest.getCustomerContactNumber());
	    lead.setCompanyName(updateLeadRequest.getCompanyName());
	    lead.setCompanyExperience(updateLeadRequest.getCompanyExperience());
	    lead.setEmploymentType(updateLeadRequest.getEmploymentType());
	    lead.setCivilScore(updateLeadRequest.getCivilScore());
	    lead.setLoanAmount(updateLeadRequest.getLoanAmount());
	    lead.setInHandSalary(updateLeadRequest.getInHandSalary());
	    lead.setCurrentEmiAmount(updateLeadRequest.getCurrentEmiAmount());
	    lead.setPfDeduction(updateLeadRequest.getPfDeduction());
	    lead.setPinCode(updateLeadRequest.getPinCode());
	    lead.setRemarks(updateLeadRequest.getRemarks());
	    lead.setServiceType(updateLeadRequest.getServiceType());
	    lead.setPanCardNumber(updateLeadRequest.getPanCardNumber());
	    lead.setStatus(updateLeadRequest.getStatus());
	    lead.setUpdatedDate(LocalDateTime.now());

	    Lead updatedLead = leadRepository.save(lead);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Lead updated successfully")
	            .response(updatedLead)
	            .build();
	}

	@Override
	public ApiResponse getLeadById(String id) {
		// TODO Auto-generated method stub
		
		Lead lead = leadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
	    LeadResponse response = this.LeadToLeadResponse(lead);

	    return ApiResponse.builder()
	            .message("Lead fetched successfully")
	            .statusCode(HttpStatus.OK.value())
	            .response(response)
	            .build();
	}

	@Override
	public ApiResponse deleteLead(String id) {
		// TODO Auto-generated method stub
		Lead lead = this.leadRepository.findByLeadIdAndIsDeleted(id, Boolean.FALSE)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.LEAD_NOT_FOUND));
		lead.setIsActive(Boolean.FALSE);
		lead.setIsDeleted(Boolean.TRUE);
		this.leadRepository.save(lead);
		return ApiResponse.builder().message(Constants.LEAD_DELETE_SUCCESS).statusCode(HttpStatus.OK.value())
				.build();
		
	}
	
	@Override
	public ApiResponse getAllLeads(Integer pageNumber, Integer pageSize, String search) {
		// TODO Auto-generated method stub
		PaginationRequest pagePaginationRequest = new PaginationRequest();
		pagePaginationRequest.setPageNumber(pageNumber);
		pagePaginationRequest.setPageSize(pageSize);
		Pageable pageableRequest = AppUtil.buildPageableRequest(pagePaginationRequest);

		Page<Lead> leads = this.leadRepository.findLeads(search, pageableRequest);

		return ApiResponse.builder().message(Constants.LEAD_FETCHED).statusCode(HttpStatus.OK.value())
				.response(new PaginatedResponse<>(leads.map(this::LeadToLeadResponse))).build();
	}


	public LeadResponse LeadToLeadResponse(Lead lead) {
		return LeadResponse.builder().civilScore(lead.getCivilScore()).companyExperience(lead.getCompanyExperience()).customerName(lead.getCustomerName())
				.serviceType(lead.getServiceType()).status(lead.getStatus())
				.employmentType(lead.getEmploymentType()).email(lead.getEmail()).companyExperience(lead.getCompanyExperience())
				.inHandSalary(lead.getInHandSalary()).pinCode(lead.getPinCode()).status(lead.getStatus()).loanAmount(lead.getLoanAmount())
				.panCardNumber(lead.getPanCardNumber()).pfDeduction(lead.getPfDeduction()).remarks(lead.getRemarks())
				.companyName(lead.getCompanyName()).customerContactNumber(lead.getCustomerContactNumber()).currentEmiAmount(lead.getCurrentEmiAmount())
				.build();
	}

	@Override
	public ApiResponse getLeadsByStatus(String status,Integer pageNumber, Integer pageSize, String search) {
		// TODO Auto-generated method stub
		 LeadStatus leadStatus;

		    try {
		        leadStatus = LeadStatus.valueOf(status.toUpperCase());
		    } catch (IllegalArgumentException e) {
		        throw new RuntimeException("Invalid Lead Status: " + status);
		    }

		    PaginationRequest pagePaginationRequest = new PaginationRequest();
		    pagePaginationRequest.setPageNumber(pageNumber);
		    pagePaginationRequest.setPageSize(pageSize);
		    Pageable pageableRequest = AppUtil.buildPageableRequest(pagePaginationRequest);

		    Page<Lead> leadsPage = leadRepository.findLeadsByStatusAndSearch(leadStatus, search, pageableRequest);
		    return ApiResponse.builder()
		            .message("Leads fetched successfully by status")
		            .statusCode(HttpStatus.OK.value())
		            .response(leadsPage)
		            .build();
	}

	@Override
	public ApiResponse updateLeadStatus(String status, String id) {
		// TODO Auto-generated method stub
		 Lead lead = leadRepository.findById(id)
		            .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

		    LeadStatus leadStatus;
		    try {
		        leadStatus = LeadStatus.valueOf(status.toUpperCase());
		    } catch (IllegalArgumentException e) {
		        throw new RuntimeException("Invalid Lead Status: " + status);
		    }

		    lead.setStatus(leadStatus);
		    lead.setUpdatedDate(LocalDateTime.now());

		    Lead updatedLead = leadRepository.save(lead);

		    return ApiResponse.builder()
		            .statusCode(HttpStatus.OK.value())
		            .message("Lead status updated successfully")
		            .response(updatedLead)
		            .build();
	}

	public ApiResponse getLeadCounts() {

        long total = leadRepository.countTotalLeads();
        long approved = leadRepository.countApproved();
        long rejected = leadRepository.countRejected();
        long underReview = leadRepository.countUnderReview();
        long newLeads = leadRepository.countNewLeads();
        long followUpLeads = leadRepository.countFollowUpLeads();

        LeadCountResponse response = new LeadCountResponse(total, approved, rejected, underReview,newLeads,followUpLeads);

//        return new ApiResponse(true, "Lead count fetched successfully", response);
        return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Lead count fetched successfully")
	            .response(response)
	            .build();
    }
}

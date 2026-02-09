package com.example.dev.serviceimpl;

import java.net.Authenticator.RequestorType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dev.exception.BadRequestException;
import com.example.dev.exception.ResourceAlreadyExistException;
import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.model.Address;
import com.example.dev.model.BusinessLoanDetails;
import com.example.dev.model.CarLoanDetails;
import com.example.dev.model.CreditCardLoanDetails;
import com.example.dev.model.EmployeementType;
import com.example.dev.model.InstantLoanDetails;
import com.example.dev.model.Lead;
import com.example.dev.model.LeadRemarksRequest;
import com.example.dev.model.LeadStatus;
import com.example.dev.model.LoanType;
import com.example.dev.model.PersonalLoanDetails;
import com.example.dev.repository.LeadRepository;
import com.example.dev.request.LeadRequest;
import com.example.dev.request.LoanTypeCountProjection;
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
	public ApiResponse createLead(LeadRequest request) {
		
		// ✅ Check Email Exists
	    if (leadRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
	        throw new ResourceAlreadyExistException("Email already exists");
	    }

	    // ✅ Check Phone Number Exists
	    if (leadRepository.existsByCustomerContactNumberAndIsDeletedFalse(request.getCustomerContactNumber())) {
	        throw new ResourceAlreadyExistException("Contact number already exists");
	    }
	    
	    if (leadRepository.existsByPanCardNumberAndIsDeletedFalse(request.getPanCardNumber())) {
	        throw new ResourceAlreadyExistException("PAN card number already exists");
	    }
	    
	    Lead lead = Lead.builder()
	            .serviceType(request.getServiceType())
	            .employmentType(request.getEmploymentType())
	            .spouseName(request.getSpouseName())
	            .motherName(request.getMotherName())
	            .maritalStatus(request.getMaritalStatus())
	            .customerName(request.getCustomerName())
	            .customerContactNumber(request.getCustomerContactNumber())
	            .email(request.getEmail())
	            .panCardNumber(request.getPanCardNumber())
	            .loanAmount(request.getLoanAmount())
	            .status(LeadStatus.NEW)
	            .isActive(Boolean.TRUE)
	            .isDeleted(Boolean.FALSE)
	            .createdDate(LocalDateTime.now())
	            .updatedDate(LocalDateTime.now())
	            .build();

	    // Address (common)
	    Address address = Address.builder()
	    		.qualification(request.getAddress().getQualification())
	    		.residenceType(request.getAddress().getResidenceType())
	            .currentAddress(request.getAddress().getCurrentAddress())
	            .currentCity(request.getAddress().getCurrentCity())
	            .currentPinCode(request.getAddress().getCurrentPinCode())
	            .currentStreetAddress(request.getAddress().getCurrentStreetAddress())
	            .permanentAddress(request.getAddress().getPermanentAddress())
	            .permanentCity(request.getAddress().getPermanentCity())
	            .permanentPinCode(request.getAddress().getPermanentPinCode())
	            .permanentStreetAddress(request.getAddress().getPermanentStreetAddress())
	            .lead(lead)
	            .build();

	    lead.setAddress(address);

	    // Loan type specific
	    if (request.getServiceType() == LoanType.PERSONAL_LOAN) {
	    	if (request.getPersonalLoanDetails()== null) {
	    	    throw new BadRequestException("Personal loan details are required");
	    	}

	        PersonalLoanDetails personal = PersonalLoanDetails.builder()
	                .companyName(request.getPersonalLoanDetails().getCompanyName())
	                .companyAddress(request.getPersonalLoanDetails().getCompanyAddress())
	                .city(request.getPersonalLoanDetails().getCity())
	                .streetAddress(request.getPersonalLoanDetails().getStreetAddress())
	                .officialEmailId(request.getPersonalLoanDetails().getOfficialEmailId())
	                .zipCode(request.getPersonalLoanDetails().getZipCode())
	                .currentEmiAmount(request.getPersonalLoanDetails().getCurrentEmiAmount())
	                .inHandSalary(request.getPersonalLoanDetails().getInHandSalary())
	                .pfDeduction(request.getPersonalLoanDetails().getPfDeduction())
	                .currentWorkExperience(request.getPersonalLoanDetails().getCurrentWorkExperience())
	                .totalWorkExperience(request.getPersonalLoanDetails().getTotalWorkExperience())
	                .designation(request.getPersonalLoanDetails().getDesignation())
	                .lead(lead)
	                .build();
	    
	        lead.setPersonalLoanDetails(personal);
	    }

	    if (request.getServiceType() == LoanType.BUSINESS_LOAN) {
	    	if (request.getBusinessLoanDetails() == null) {
	    	    throw new BadRequestException("Business loan details are required");
	    	}

	    	BusinessLoanDetails business = BusinessLoanDetails.builder()
	                .businessName(request.getBusinessLoanDetails().getBusinessName())
	                .businessAddress(request.getBusinessLoanDetails().getBusinessAddress())
	                .streetAddress(request.getBusinessLoanDetails().getStreetAddress())
	                .city(request.getBusinessLoanDetails().getCity())
	                .zipCode(request.getBusinessLoanDetails().getZipCode())
	                .role(request.getBusinessLoanDetails().getRole())
	                .businessVintage(request.getBusinessLoanDetails().getBusinessVintage())
	                .monthlyIncome(request.getBusinessLoanDetails().getMonthlyIncome())
	                .gstRegistered(request.getBusinessLoanDetails().getGstRegistered())
	                .itrFiled(request.getBusinessLoanDetails().getItrFiled())
	                .lead(lead)
	                .build();

	        lead.setBusinessLoanDetails(business);
	    }
	    if (request.getServiceType() == LoanType.INSTANT_LOAN) {

	        if (request.getInstantLoanDetails() == null) {
	            throw new BadRequestException("Instant loan details are required");
	        }

	        InstantLoanDetails instantLoan = InstantLoanDetails.builder()
	                .companyName(request.getInstantLoanDetails().getCompanyName())
	                .companyAddress(request.getInstantLoanDetails().getCompanyAddress())
	                .streetAddress(request.getInstantLoanDetails().getStreetAddress())
	                .city(request.getInstantLoanDetails().getCity())
	                .zipCode(request.getInstantLoanDetails().getZipCode())
	                .designation(request.getInstantLoanDetails().getDesignation())
	                .officialEmailId(request.getInstantLoanDetails().getOfficialEmailId())
	                .currentWorkExperience(request.getInstantLoanDetails().getCurrentWorkExperience())
	                .totalWorkExperience(request.getInstantLoanDetails().getTotalWorkExperience())
	                .monthlyInHandSalary(request.getInstantLoanDetails().getMonthlyInHandSalary())
	                .pfDeduction(request.getInstantLoanDetails().getPfDeduction())
	                .lead(lead)
	                .build();

	        lead.setInstantLoanDetails(instantLoan);
	    }
	    
	    if (request.getServiceType() == LoanType.CAR_LOAN) {

	        if (request.getCarLoanDetails() == null) {
	            throw new BadRequestException("Car loan details are required");
	        }

	        CarLoanDetails carLoan = CarLoanDetails.builder()
	                .companyName(request.getCarLoanDetails().getCompanyName())
	                .companyAddress(request.getCarLoanDetails().getCompanyAddress())
	                .streetAddress(request.getCarLoanDetails().getStreetAddress())
	                .city(request.getCarLoanDetails().getCity())
	                .zipCode(request.getCarLoanDetails().getZipCode())
	                .designation(request.getCarLoanDetails().getDesignation())
	                .officialEmailId(request.getCarLoanDetails().getOfficialEmailId())
	                .currentWorkExperience(request.getCarLoanDetails().getCurrentWorkExperience())
	                .totalWorkExperience(request.getCarLoanDetails().getTotalWorkExperience())
	                .monthlyInHandSalary(request.getCarLoanDetails().getMonthlyInHandSalary())
	                .pfDeduction(request.getCarLoanDetails().getPfDeduction())
	                .lead(lead)
	                .build();

	        lead.setCarLoanDetails(carLoan);
	    }
	    if (request.getServiceType() == LoanType.CREDIT_CARD) {

	        if (request.getCreditCardLoanDetails() == null) {
	            throw new BadRequestException("Credit card loan details are required");
	        }

	        CreditCardLoanDetails creditCard = CreditCardLoanDetails.builder()
	                .companyName(request.getCreditCardLoanDetails().getCompanyName())
	                .companyAddress(request.getCreditCardLoanDetails().getCompanyAddress())
	                .streetAddress(request.getCreditCardLoanDetails().getStreetAddress())
	                .city(request.getCreditCardLoanDetails().getCity())
	                .zipCode(request.getCreditCardLoanDetails().getZipCode())
	                .designation(request.getCreditCardLoanDetails().getDesignation())
	                .officialEmailId(request.getCreditCardLoanDetails().getOfficialEmailId())
	                .currentWorkExperience(request.getCreditCardLoanDetails().getCurrentWorkExperience())
	                .totalWorkExperience(request.getCreditCardLoanDetails().getTotalWorkExperience())
	                .monthlyInHandSalary(request.getCreditCardLoanDetails().getMonthlyInHandSalary())
	                .pfDeduction(request.getCreditCardLoanDetails().getPfDeduction())
	                .lead(lead)
	                .build();

	        lead.setCreditCardLoanDetails(creditCard);
	    }



	    Lead saved = leadRepository.save(lead);

	    return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
				.message(Constants.LEAD_CREATED_SUCCESSFULLY).response(saved).build();
	    }

	@Override
	public ApiResponse updateLead(UpdateLeadRequest updateLeadRequest) {
		// TODO Auto-generated method stub
//		Lead lead = leadRepository.findByLeadIdAndIsDeleted(updateLeadRequest.getId(),Boolean.FALSE)
//	            .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + updateLeadRequest.getId()));
//
//	    // ✅ Check Email Exists (except current lead)
//	    if (!lead.getEmail().equals(updateLeadRequest.getEmail())
//	            && leadRepository.existsByEmailAndIsDeletedFalse(updateLeadRequest.getEmail())) {
//	        throw new ResourceAlreadyExistException("Email already exists");
//	    }
//
//	    // ✅ Check Contact Number Exists (except current lead)
//	    if (!lead.getCustomerContactNumber().equals(updateLeadRequest.getCustomerContactNumber())
//	            && leadRepository.existsByCustomerContactNumberAndIsDeletedFalse(updateLeadRequest.getCustomerContactNumber())) {
//	        throw new ResourceAlreadyExistException("Contact number already exists");
//	    }
//
//	    // ✅ Check PAN Exists (except current lead)
//	    if (!lead.getPanCardNumber().equals(updateLeadRequest.getPanCardNumber())
//	            && leadRepository.existsByPanCardNumberAndIsDeletedFalse(updateLeadRequest.getPanCardNumber())) {
//	        throw new ResourceAlreadyExistException("PAN card number already exists");
//	    }
//
//	    // ✅ Update Fields
//	    lead.setCustomerName(updateLeadRequest.getCustomerName());
//	    lead.setEmail(updateLeadRequest.getEmail());
//	    lead.setCustomerContactNumber(updateLeadRequest.getCustomerContactNumber());
//	    lead.setCompanyName(updateLeadRequest.getCompanyName());
//	    lead.setCompanyExperience(updateLeadRequest.getCompanyExperience());
//	    lead.setEmploymentType(updateLeadRequest.getEmploymentType());
//	    lead.setCivilScore(updateLeadRequest.getCivilScore());
//	    lead.setLoanAmount(updateLeadRequest.getLoanAmount());
//	    lead.setInHandSalary(updateLeadRequest.getInHandSalary());
//	    lead.setCurrentEmiAmount(updateLeadRequest.getCurrentEmiAmount());
//	    lead.setPfDeduction(updateLeadRequest.getPfDeduction());
//	    lead.setPinCode(updateLeadRequest.getPinCode());
//	    lead.setRemarks(updateLeadRequest.getRemarks());
//	    lead.setServiceType(updateLeadRequest.getServiceType());
//	    lead.setPanCardNumber(updateLeadRequest.getPanCardNumber());
//	    lead.setStatus(updateLeadRequest.getStatus());
//	    lead.setUpdatedDate(LocalDateTime.now());
//
//	    Lead updatedLead = leadRepository.save(lead);
//
//	    return ApiResponse.builder()
//	            .statusCode(HttpStatus.OK.value())
//	            .message("Lead updated successfully")
//	            .response(updatedLead)
//	            .build();
//	}
//
//	@Override
//	public ApiResponse getLeadById(String id) {
//		// TODO Auto-generated method stub
//		
//		Lead lead = leadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
//	    LeadResponse response = this.LeadToLeadResponse(lead);
//
//	    return ApiResponse.builder()
//	            .message("Lead fetched successfully")
//	            .statusCode(HttpStatus.OK.value())
//	            .response(response)
//	            .build();
		return null;
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
				.response(new PaginatedResponse<>(leads.map(this::leadToLeadResponse))).build();
	}


	public LeadResponse leadToLeadResponse(Lead lead) {

	    return LeadResponse.builder()
	            .status(lead.getStatus())
	            .customerName(lead.getCustomerName())
	            .customerContactNumber(lead.getCustomerContactNumber())
	            .email(lead.getEmail())
	            .panCardNumber(lead.getPanCardNumber())
	            .loanAmount(lead.getLoanAmount())
	            .employmentType(lead.getEmploymentType())
	            .maritalStatus(lead.getMaritalStatus())
	            .spouseName(lead.getSpouseName())
	            .motherName(lead.getMotherName())
	            .serviceType(lead.getServiceType())
	            .remarks(lead.getRemarks())
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

	
		@Override
		public ApiResponse getRecentLeads() {
		    List<Lead> leads =
		            leadRepository.findRecentLeads();

		    return ApiResponse.builder().message(Constants.LEAD_FETCHED).statusCode(HttpStatus.OK.value()).response(leads.stream()
		     .map(this::leadToLeadResponse).toList()).build();

	}
		@Override
		public ApiResponse getLoanTypeChartData() {

		    List<LoanTypeCountProjection> data =
		            leadRepository.countLeadsByLoanType();

		    List<Map<String, Object>> response = data.stream().map(item -> {
		                Map<String, Object> map = new HashMap<>();
		                map.put("loanType", item.getLoanType());
		                map.put("count", item.getCount());
		                return map;
		            })
		            .toList();

		    return ApiResponse.builder()
		            .message("Loan type chart data fetched")
		            .statusCode(HttpStatus.OK.value())
		            .response(response)
		            .build();
		}
		@Override
		public ApiResponse addLeadRemarks(String leadId, LeadRemarksRequest request) {

		    Lead lead = leadRepository.findActiveLead(leadId).orElseThrow(() -> new ResourceNotFoundException("Lead not found"));
		    lead.setRemarks(request.getRemarks());
		    lead.setUpdatedDate(LocalDateTime.now());
		    leadRepository.save(lead);

		    return ApiResponse.builder().statusCode(HttpStatus.OK.value()).message("Remarks added successfully").response(Map.of(
		                    "leadId", lead.getLeadId(),
		                    "remarks", lead.getRemarks()
		            ))
		            .build();
		}

		@Override
		public ApiResponse getLeadById(String id) {
			// TODO Auto-generated method stub
			return null;
		}


}

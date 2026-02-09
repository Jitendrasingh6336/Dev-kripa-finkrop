package com.example.dev.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dev.exception.ResourceAlreadyExistException;
import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.model.CareerApplication;
import com.example.dev.model.CareerStatus;
import com.example.dev.model.Faq;
import com.example.dev.model.Lead;
import com.example.dev.repository.CareerRepository;
import com.example.dev.request.CarrerRequest;
import com.example.dev.request.PaginationRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.CareerResponse;
import com.example.dev.response.CareerStatusCountResponse;
import com.example.dev.response.PaginatedResponse;
import com.example.dev.service.ICareerService;
import com.example.dev.util.AppUtil;
import com.example.dev.util.Constants;

@Service
public class CarrerServiceImpl implements ICareerService{

	@Autowired
	private CareerRepository careerRepository;
	
	@Autowired
	private AppUtil appUtil;
	
	@Override
	public ApiResponse addCareerApplication(CarrerRequest request) {
		// TODO Auto-generated method stub
		if (careerRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
	        throw new ResourceAlreadyExistException("Email already exists");
	    }

	    // Phone check
	    if (careerRepository.existsByPhoneAndIsDeletedFalse(request.getPhone())) {
	        throw new ResourceAlreadyExistException("Phone number already exists");
	    }
		CareerApplication career = CareerApplication.builder().name(request.getName()).email(request.getEmail()).experience(request.getExperience())
				.message(request.getMessage()).phone(request.getPhone()).position(request.getPosition()).qualification(request.getQualification())
				.status(request.getStatus()).createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now())
	            .isDeleted(Boolean.FALSE).isActive(Boolean.TRUE)
	            .build();
		if (request.getResumeUrl() != null) {
			String fileName = this.appUtil.uploadPhoto(request.getResumeUrl(), Constants.CAREER_APPLICATION_IMG);
			career.setResumeUrl(fileName);
		} 

	    CareerApplication savedCarrerApplication = careerRepository.save(career);

	    return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
				.message(Constants.CAREER_APPLICATION_CREATED_SUCCESSFULLY).response(savedCarrerApplication).build();
		
	}

	@Override
	public ApiResponse getAllCareerApplication(Integer pageNumber, Integer pageSize, String search) {

	    PaginationRequest pagePaginationRequest = new PaginationRequest();
	    pagePaginationRequest.setPageNumber(pageNumber);
	    pagePaginationRequest.setPageSize(pageSize);

	    Pageable pageableRequest = AppUtil.buildPageableRequest(pagePaginationRequest);

	    Page<CareerApplication> careers =careerRepository.findCareerApplications(search, pageableRequest);

	    return ApiResponse.builder()
	            .message(Constants.CAREER_FETCHED)
	            .statusCode(HttpStatus.OK.value())
	            .response(new PaginatedResponse<>(
	                    careers.map(this::careerToCareerResponse)
	            )).build();
	}
	private CareerResponse careerToCareerResponse(CareerApplication career) {

	    return CareerResponse.builder()
	            .id(career.getId())
	            .name(career.getName())
	            .email(career.getEmail())
	            .phone(career.getPhone())
	            .position(career.getPosition())
	            .experience(career.getExperience())
	            .qualification(career.getQualification())
	            .message(career.getMessage())
	            .resumeUrl(career.getResumeUrl())
	            .status(career.getStatus())
	            .build();
	}
	
	@Override
	public ApiResponse getCareerStatusCounts() {

	    long newCount =
	            careerRepository.countByStatusAndIsDeletedFalse(CareerStatus.NEW);

	    long interviewCount =
	            careerRepository.countByStatusAndIsDeletedFalse(CareerStatus.INTERVIEW);

	    long shortlistedCount =
	            careerRepository.countByStatusAndIsDeletedFalse(CareerStatus.SHORTLISTED);

	    long selectedCount =
	            careerRepository.countByStatusAndIsDeletedFalse(CareerStatus.SELECTED);

	    long total = newCount + interviewCount + shortlistedCount + selectedCount;

	    CareerStatusCountResponse response = CareerStatusCountResponse.builder()
	            .total(total)
	            .newCount(newCount)
	            .interviewCount(interviewCount)
	            .shortlistedCount(shortlistedCount)
	            .selectedCount(selectedCount)
	            .build();

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Career application status count fetched successfully")
	            .response(response)
	            .build();
	}

	@Override
	public ApiResponse getCareerApplicationById(String id) {
		// TODO Auto-generated method stub
		CareerApplication careerApplication = careerRepository
	            .findByIdAndIsDeleted(id,Boolean.FALSE).orElseThrow(() -> new ResourceNotFoundException("Carrer application not found"));

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Career application fetched successfully")
	            .response(careerApplication)
	            .build();
	}

	@Override
	public ApiResponse deleteCareerApplication(String id) {
		// TODO Auto-generated method stub
		CareerApplication career = this.careerRepository.findByIdAndIsDeleted(id, Boolean.FALSE)
				.orElseThrow(() -> new ResourceNotFoundException("Carrer application not found"));
		career.setIsActive(Boolean.FALSE);
		career.setIsDeleted(Boolean.TRUE);
		this.careerRepository.save(career);
		return ApiResponse.builder().message(Constants.CAREER_APPLICATION_DELETE_SUCCESS).statusCode(HttpStatus.OK.value())
				.build();
	}


}

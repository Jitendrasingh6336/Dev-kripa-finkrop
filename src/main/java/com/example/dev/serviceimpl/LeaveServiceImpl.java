package com.example.dev.serviceimpl;

import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.model.Employee1;
import com.example.dev.model.Faq;
import com.example.dev.model.Lead;
import com.example.dev.model.LeadStatus;
import com.example.dev.model.Leave;
import com.example.dev.model.LeaveStatus;
import com.example.dev.model.LeaveType;
import com.example.dev.model.LoanType;
import com.example.dev.repository.Employee1Repository;
import com.example.dev.repository.LeaveRepository;
import com.example.dev.request.LeaveRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.LeaveBalanceResponse;
import com.example.dev.response.LeaveResponse;
import com.example.dev.response.LeaveStatusCountResponse;
import com.example.dev.response.PaginatedResponse;
import com.example.dev.service.ILeaveService;
import com.example.dev.util.AppUtil;
import com.example.dev.util.Constants;
import com.opencsv.CSVReader;

@Service
public class LeaveServiceImpl implements ILeaveService{

	@Autowired
	private LeaveRepository leaveRepository;
	
	@Autowired
	private Employee1Repository employee1Repository;
	
	@Override
	public ApiResponse addLeave(LeaveRequest leaveRequest) {
		// TODO Auto-generated method stub
		  Employee1 employee = employee1Repository.findByIdAndIsDeleted(leaveRequest.getEmployeeId(),Boolean.FALSE)
	                .orElseThrow(() -> new RuntimeException("Employee not found"));

	        long totalDays = ChronoUnit.DAYS.between(
	                leaveRequest.getFromDate(),
	                leaveRequest.getToDate()) + 1;

	        Leave leave = Leave.builder()
	                .employee(employee)
	                .leaveType(leaveRequest.getLeaveType())
	                .fromDate(leaveRequest.getFromDate())
	                .toDate(leaveRequest.getToDate())
	                .reason(leaveRequest.getReason())
	                .totalDays((int) totalDays)
	                .status(LeaveStatus.PENDING)
	                .isDeleted(false).isActive(true).createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now())
	                .build();

	        leaveRepository.save(leave);

	        return ApiResponse.builder()
	                .message("Leave applied successfully")
	                .statusCode(201)
	                .response(leave)
	                .build();
	    }
		

	@Override
	public ApiResponse getAllLeave(Integer pageNumber, Integer pageSize, String search) {
		// TODO Auto-generated method stub
		 Pageable pageable = AppUtil.getPageable(pageNumber, pageSize);
		 Page<Leave> leavePage;

		    if (search != null && !search.trim().isEmpty()) {
		        leavePage = leaveRepository.searchLeaves(search.trim(), pageable);
		    } else {
		        leavePage = leaveRepository.findByIsDeletedFalse(pageable);
		    }

		    Page<LeaveResponse> responsePage =
		            leavePage.map(this::leaveToLeaveResponse);

		    return ApiResponse.builder()
		            .statusCode(HttpStatus.OK.value())
		            .message("Leave fetched successfully")
		            .response(new PaginatedResponse<>(responsePage))
		            .build();
	}

	@Override
	public ApiResponse deleteLeave(String id) {
		// TODO Auto-generated method stub
		Leave leave = this.leaveRepository.findByIdAndIsDeleted(id, Boolean.FALSE)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.LEAVE_NOT_FOUND));
		leave.setIsActive(Boolean.FALSE);
		leave.setIsDeleted(Boolean.TRUE);
		this.leaveRepository.save(leave);
		return ApiResponse.builder().message(Constants.LEAVE_DELETE_SUCCESS).statusCode(HttpStatus.OK.value())
				.build();
		
	}


	@Override
	public ApiResponse getLeaveBalance(String employeeId) {
		// TODO Auto-generated method stub
		List<Object[]> results = leaveRepository.getLeaveCountByEmployee(employeeId);

	    int casualUsed = 0;
	    int sickUsed = 0;
	    int earnedUsed = 0;
	    int unpaidUsed = 0;

	    for (Object[] row : results) {

	        LeaveType type = (LeaveType) row[0];
	        Long days = (Long) row[1];

	        switch (type) {
	            case CASUAL -> casualUsed = days.intValue();
	            case SICK -> sickUsed = days.intValue();
	            case EARNED -> earnedUsed = days.intValue();
	            case UNPAID -> unpaidUsed = days.intValue();
	        }
	    }

	    // Fixed limits (you can also store in DB later)
	    int casualTotal = 12;
	    int sickTotal = 10;
	    int earnedTotal = 15;
	    int unpaidTotal = 30;

	    LeaveBalanceResponse response = new LeaveBalanceResponse(

	            casualUsed,
	            casualTotal,
	            casualTotal - casualUsed,

	            sickUsed,
	            sickTotal,
	            sickTotal - sickUsed,

	            earnedUsed,
	            earnedTotal,
	            earnedTotal - earnedUsed,

	            unpaidUsed,
	            unpaidTotal,
	            unpaidTotal - unpaidUsed);
	    return ApiResponse.builder()
                .message("Leave balance fetched successfully")
                .statusCode(200)
                .response(response)
                .build();
	}

	public LeaveResponse leaveToLeaveResponse(Leave leave) {

	    return LeaveResponse.builder()
	            .id(leave.getId())
	            .leaveType(leave.getLeaveType())
	            .fromDate(leave.getFromDate())
	            .toDate(leave.getToDate())
	            .totalDays(leave.getTotalDays())
	            .status(leave.getStatus())
	            .reason(leave.getReason())
	            .employeeName(
	                    leave.getEmployee() != null
	                            ? leave.getEmployee().getName()
	                            : null
	            )
	            .build();
	}


	@Override
	public ApiResponse getLeaveStatusCount() {

	    List<Object[]> results = leaveRepository.countLeaveByStatus();
	    long pending = 0;
	    long approved = 0;
	    long rejected = 0;

	    for (Object[] row : results) {

	        LeaveStatus status = (LeaveStatus) row[0];
	        Long count = (Long) row[1];

	        switch (status) {
	            case PENDING -> pending = count;
	            case APPROVED -> approved = count;
	            case REJECTED -> rejected = count;
	            default -> {}
	        }
	    }

	    long total = leaveRepository.countByIsDeletedFalse();

	    LeaveStatusCountResponse response = LeaveStatusCountResponse.builder()
	            .totalRequests(total)
	            .pending(pending)
	            .approved(approved)
	            .rejected(rejected)
	            .build();

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Leave status count fetched successfully")
	            .response(response)
	            .build();
	
	}


}

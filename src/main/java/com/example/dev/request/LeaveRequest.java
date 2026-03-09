package com.example.dev.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.dev.model.Employee1;
import com.example.dev.model.LeaveStatus;
import com.example.dev.model.LeaveType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class LeaveRequest {

	    private LeaveType leaveType;

	    private LocalDate fromDate;

	    private LocalDate toDate;

	    private String reason;

	    private Integer totalDays;

	    private String employeeId;
	    
	    private LocalDateTime createdDate;

	    private LocalDateTime updatedDate;

	    private Boolean isDeleted = false;
	    
	    private Boolean isActive;
}

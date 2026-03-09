package com.example.dev.response;

import java.time.LocalDate;

import com.example.dev.model.LeaveStatus;
import com.example.dev.model.LeaveType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaveResponse {

	private String id;
    private LeaveType leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer totalDays;
    private LeaveStatus status;
    private String reason;
    private String employeeName;
}

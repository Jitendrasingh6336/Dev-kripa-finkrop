package com.example.dev.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dev.model.Attendance;
import com.example.dev.model.AttendanceStatus;
import com.example.dev.model.Employee1;
import com.example.dev.repository.AttendanceRepository;
import com.example.dev.repository.Employee1Repository;
import com.example.dev.request.AttendanceRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.IAttendanceService;
import com.example.dev.util.Constants;

@Service
public class AttendanceServiceImpl implements IAttendanceService{

	@Autowired
	private Employee1Repository employee1Repository;
	
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	@Override
	public ApiResponse addAttendance(AttendanceRequest attendanceRequest) {
		 Employee1 employee = employee1Repository.findByIdAndIsDeleted(attendanceRequest.getEmployeeId(),Boolean.FALSE)
	                .orElseThrow(() -> new RuntimeException("Employee not found"));

	        LocalDate today = LocalDate.now();

	        // Check already marked
	        attendanceRepository.findByEmployeeIdAndDate(employee.getId(), today)
	                .ifPresent(a -> {
	                    throw new RuntimeException("Attendance already marked today");
	                });

	        Attendance attendance = Attendance.builder()
	                .employee(employee)
	                .date(today)
	                .checkIn(LocalDateTime.now())
	                .status(AttendanceStatus.PRESENT)
	                .build();

	      Attendance savedAttendance=attendanceRepository.save(attendance);
	        return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
					.message(Constants.ATTENDANCE_ADD_SUCCESSFULLY).response(savedAttendance).build();
	}

	@Override
	public ApiResponse clockOut(String employeeId) {
		// TODO Auto-generated method stub
		 LocalDate today = LocalDate.now();

	        Attendance attendance = attendanceRepository
	                .findByEmployeeIdAndDate(employeeId, today)
	                .orElseThrow(() -> new RuntimeException("Clock In first"));

	        attendance.setCheckOut(LocalDateTime.now());

	        long minutes = java.time.Duration.between(
	                attendance.getCheckIn(),
	                attendance.getCheckOut()
	        ).toMinutes();

	        double hours = minutes / 60.0;

	        attendance.setWorkingHours(hours);

	        attendanceRepository.save(attendance);
	        return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
					.message(Constants.ATTENDANCE_ADD_SUCCESSFULLY).build();
	}

	
}

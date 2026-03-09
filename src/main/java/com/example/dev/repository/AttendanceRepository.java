package com.example.dev.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dev.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, String>{

	Optional<Attendance> findByEmployeeIdAndDate(String employeeId, LocalDate date);
}


package com.example.dev.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Attendance {

	    @Id
	    @GeneratedValue(strategy = GenerationType.UUID)
	    private String id;

	    private LocalDate date;

	    private LocalDateTime checkIn;

	    private LocalDateTime checkOut;

	    private Double workingHours;

	    @Enumerated(EnumType.STRING)
	    private AttendanceStatus status; // PRESENT, ABSENT, LEAVE

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "employee_id")
	    private Employee1 employee;

}

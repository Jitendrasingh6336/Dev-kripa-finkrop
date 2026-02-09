package com.example.dev.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalLoanDetails {

	    @Id
	    @GeneratedValue(strategy = GenerationType.UUID)
	    private String id;

	    @Column(nullable = false)
	    private String companyName;

	    @Column(nullable = false)
	    private String companyAddress;

	    private String streetAddress;

	    @Column(nullable = false)
	    private String city;

	    @Column(nullable = false)
	    private String zipCode;

	    @Column(nullable = false)
	    private String designation;

	    private String officialEmailId;

	    @Column(nullable = false)
	    private Integer currentWorkExperience;

	    @Column(nullable = false)
	    private Integer totalWorkExperience;

	    private Double inHandSalary;

	    private Boolean pfDeduction;

	    private Double currentEmiAmount;

	    @OneToOne
	    @JoinColumn(name = "lead_id", nullable = false)
	    private Lead lead;
}

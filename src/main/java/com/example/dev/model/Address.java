package com.example.dev.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String addressId;

	 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanQualification qualification;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResidenceType residenceType;
    
    private String currentAddress;
    private String currentCity;
    private String currentPinCode;
    private String currentStreetAddress;


    private String permanentAddress;
    private String permanentCity;
    private String permanentPinCode;
    private String permanentStreetAddress;


    @OneToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;
}

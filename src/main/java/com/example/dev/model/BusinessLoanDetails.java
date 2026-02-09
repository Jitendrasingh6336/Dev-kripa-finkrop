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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class BusinessLoanDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String businessAddress;

    private String streetAddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessVintage businessVintage;


    @Column(nullable = false)
    private Double monthlyIncome;

   
    @Column(nullable = false)
    private Boolean gstRegistered;

   
    @Column(nullable = false)
    private Boolean itrFiled;

    // Example mapping
    @OneToOne
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;
}

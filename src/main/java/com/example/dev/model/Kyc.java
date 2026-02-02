package com.example.dev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kyc {

	
	    @Id
	    @GeneratedValue(strategy = GenerationType.UUID)
	    private String id;

	    private String panNumber;
	    private String aadhaarNumber;
	    private String bankAccount;
}

package com.example.dev.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadRemarksRequest {

	 @NotBlank(message = "Remarks cannot be empty")
	 private String remarks;
}

package com.example.dev.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentRequest {

	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotBlank(message = "Name is description")
	private String description;
}

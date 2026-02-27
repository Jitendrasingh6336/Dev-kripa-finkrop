package com.example.dev.request;

import lombok.Data;

@Data
public class UpdateDepartmentRequest {

	private String id;
	
	private String name;
	
	private String description;
}

package com.example.dev.request;

import java.util.List;

import lombok.Data;

@Data
public class AssignLeadRequest {

	 private List<String> leadIds;
	 private String managerId;
}

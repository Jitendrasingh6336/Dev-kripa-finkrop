package com.example.dev.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeNameResponse {

	private String firstName;
    private String lastName;
}

package com.example.dev.response;

import com.example.dev.model.LoanType;
import com.example.dev.model.QueryStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerQueryResponse {

	    private String id;
	    private String username;
	    private String email;
	    private String message;
	    private LoanType serviceType;
	    private QueryStatus queryStatus;
}

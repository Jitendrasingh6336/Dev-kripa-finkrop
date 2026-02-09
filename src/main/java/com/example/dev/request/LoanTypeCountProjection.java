package com.example.dev.request;

import com.example.dev.model.LoanType;

public interface LoanTypeCountProjection {

	LoanType getLoanType();
    Long getCount();
}

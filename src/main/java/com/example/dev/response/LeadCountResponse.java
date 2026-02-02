package com.example.dev.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadCountResponse {

	    private long totalLeads;
	    private long approvedLeads;
	    private long rejectedLeads;
	    private long underReviewLeads;
	    private long newLeads;
	    private long followUpLeads;
}

package com.example.dev.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CareerStatusCountResponse {
	
	private long total;
    private long newCount;
    private long interviewCount;
    private long shortlistedCount;
    private long selectedCount;

}

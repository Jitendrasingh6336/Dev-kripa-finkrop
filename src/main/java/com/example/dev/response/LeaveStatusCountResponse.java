package com.example.dev.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LeaveStatusCountResponse {

	private Long totalRequests;
    private Long pending;
    private Long approved;
    private Long rejected;
}

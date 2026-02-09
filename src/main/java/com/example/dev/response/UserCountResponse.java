package com.example.dev.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCountResponse {

	private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
}

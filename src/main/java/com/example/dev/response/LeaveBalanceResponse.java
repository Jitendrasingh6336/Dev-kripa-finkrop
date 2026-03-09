package com.example.dev.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaveBalanceResponse {

	private Integer casualUsed;
    private Integer casualTotal;
    private Integer casualRemaining;

    private Integer sickUsed;
    private Integer sickTotal;
    private Integer sickRemaining;

    private Integer earnedUsed;
    private Integer earnedTotal;
    private Integer earnedRemaining;

    private Integer unpaidUsed;
    private Integer unpaidTotal;
    private Integer unpaidRemaining;

}

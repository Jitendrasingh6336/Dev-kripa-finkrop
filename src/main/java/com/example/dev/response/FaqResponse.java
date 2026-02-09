package com.example.dev.response;

import com.example.dev.model.FaqCategory;
import com.example.dev.model.FaqStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaqResponse {

	private String faqId;
    private String question;
    private String answer;
    private FaqCategory category;
    private FaqStatus status;
}

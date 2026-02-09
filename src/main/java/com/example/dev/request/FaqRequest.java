package com.example.dev.request;

import com.example.dev.model.FaqCategory;
import com.example.dev.model.FaqStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FaqRequest {
	
	@NotBlank(message = "Question is required")
	private String question;

	@NotBlank(message = "Answer is required")
	private String answer;

	@NotNull(message = "Category is required")
	private FaqCategory category;

	private FaqStatus status;

}

package com.example.dev.service;

import com.example.dev.request.FaqRequest;
import com.example.dev.request.UpdateFaqRequest;
import com.example.dev.response.ApiResponse;

public interface IFaqService {
	
	public ApiResponse addFaq(FaqRequest faqRequest);

	public ApiResponse getAllFaq(Integer pageNumber, Integer pageSize, String search);
	
	public ApiResponse updateFaq(UpdateFaqRequest updateFaqRequest);
	
	public ApiResponse deleteFaq(String id);
	
	
}

package com.example.dev.service;

import com.example.dev.request.CarrerRequest;
import com.example.dev.response.ApiResponse;

public interface ICareerService {
	
	public ApiResponse addCareerApplication(CarrerRequest careeCarrerRequest);
	
	public ApiResponse getAllCareerApplication(Integer pageNumber, Integer pageSize, String search);
	
	public ApiResponse getCareerStatusCounts();
	
	public ApiResponse getCareerApplicationById(String id);
	
	public ApiResponse deleteCareerApplication(String id);


}

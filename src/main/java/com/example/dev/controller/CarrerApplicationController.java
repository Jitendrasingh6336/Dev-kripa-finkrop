package com.example.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dev.request.CarrerRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.service.ICareerService;
import com.example.dev.util.Constants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/career")
@CrossOrigin("*")
@Validated
public class CarrerApplicationController {

	@Autowired
	private ICareerService careerService;
	
	@PostMapping("/api/v1/create")
    public ApiResponse createCareerApplication(@Valid CarrerRequest request) {
        return careerService.addCareerApplication(request);
    }
    
    @GetMapping("/api/v1/get-careers-data")
    public ResponseEntity<ApiResponse> getLeads(
    		   @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
               @RequestParam(defaultValue = Constants.DEFAULT_PAGE_LIMIT, required = false) Integer pageSize,
               @RequestParam(defaultValue = "", required = false) String search) {
        return ResponseEntity.ok(this.careerService.getAllCareerApplication(pageNumber, pageSize, search));
    }
    
    
    @GetMapping("/api/v1/get-count")
    public ResponseEntity<ApiResponse> getCareerApplicationCount() {
        return ResponseEntity.ok(this.careerService.getCareerStatusCounts());
    }
    
    //delete career application
    @DeleteMapping("api/v1/delete")
    public ResponseEntity<ApiResponse> deleteCareerApplication(
            @RequestParam String id) {

        ApiResponse response = careerService.deleteCareerApplication(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/api/v1/get-career-by-id")
	public ResponseEntity<ApiResponse> getCareerApplication(@RequestParam("id") String id){
		return ResponseEntity.ok(this.careerService.getCareerApplicationById(id));
	}
}

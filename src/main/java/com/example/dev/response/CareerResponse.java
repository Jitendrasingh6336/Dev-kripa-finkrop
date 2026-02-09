package com.example.dev.response;

import com.example.dev.model.CareerStatus;
import com.example.dev.model.Experience;
import com.example.dev.model.Position;
import com.example.dev.model.Qualification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CareerResponse {

	    private String id;
	    private String name;
	    private String email;
	    private String phone;
	    private Position position;
	    private Experience experience;
	    private Qualification qualification;
	    private String message;
	    private String resumeUrl;
	    private CareerStatus status;
}

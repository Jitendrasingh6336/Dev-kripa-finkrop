package com.example.dev.request;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.example.dev.model.CareerStatus;
import com.example.dev.model.Experience;
import com.example.dev.model.Position;
import com.example.dev.model.Qualification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarrerRequest {

	@NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@gmail\\.com$",
        message = "Only Gmail addresses are allowed"
    )
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Phone number must be 10 digits"
    )
    private String phone;

    @NotNull(message = "Position is required")
    private Position position;

    @NotNull(message = "Experience is required")
    private Experience experience;

    @NotNull(message = "Qualification is required")
    private Qualification qualification;

    private String message;

    @NotNull(message = "Resume is required")
    private MultipartFile resumeUrl;

    @NotNull(message = "Status is required")
    private CareerStatus status;
    
    private LocalDateTime createdDate;
    
    private LocalDateTime updatedDate;

    private Boolean isDeleted = Boolean.FALSE;
    private Boolean isActive = Boolean.TRUE;
}

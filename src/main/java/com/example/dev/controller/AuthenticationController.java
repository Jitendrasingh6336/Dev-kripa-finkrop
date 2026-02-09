package com.example.dev.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.model.ChangePasswordRequest;
import com.example.dev.repository.UserRepository;
import com.example.dev.request.LoginRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.UserResponse;
import com.example.dev.service.IUserService;
import com.example.dev.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Validated
public class AuthenticationController {
	
private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final IUserService userService;
    
    @Autowired
    private UserRepository userRepository;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

   
    @PostMapping("/api/v1/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {

        Map<String, Object> response = new HashMap<>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            String token = jwtUtil.generateToken(loginRequest.getEmail());
            UserResponse userResponse = userService.getUserByUsername(loginRequest.getEmail());

            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("user", userResponse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @GetMapping("/api/v1/admin/profile")
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        String email = authentication.getName();
        UserResponse user = userService.getUserByUsername(email);
        response.put("success", true);
        response.put("message", "Profile fetched successfully");
        response.put("user", user);
        return ResponseEntity.ok(response);
        
    }
    
    @PostMapping("/api/v1/admin/change-password")
    public  ResponseEntity<ApiResponse> changePassword(@Valid
            @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(this. userService.changePassword(email, request));

    }


}

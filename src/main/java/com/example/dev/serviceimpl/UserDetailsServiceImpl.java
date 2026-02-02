package com.example.dev.serviceimpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.dev.model.Lead;
import com.example.dev.model.Role;
import com.example.dev.model.User;
import com.example.dev.repository.UserRepository;
import com.example.dev.request.UserRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.UserResponse;
import com.example.dev.service.IUserService;
import com.example.dev.util.Constants;

@Service
public class UserDetailsServiceImpl implements UserDetailsService,IUserService{

	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

	    // Convert role to GrantedAuthority
	    List<GrantedAuthority> authorities = List.of(
	            new SimpleGrantedAuthority("ROLE_" + user.getRole())
	    );

	    return new org.springframework.security.core.userdetails.User(
	            user.getEmail(),
	            user.getPassword(),
	            authorities
	    );
	}

	@Override
	public ApiResponse addUser(UserRequest userRequest) {
		 // 1️⃣ Check Email Already Exists
	    if (userRepository.existsByEmail(userRequest.getEmail())) {
	        return ApiResponse.builder()
	                .statusCode(HttpStatus.BAD_REQUEST.value())
	                .message("Email already exists!")
	                .build();
	    }

	    // 2️⃣ Check Only One ADMIN Allowed
	    if (userRequest.getRole() == Role.ADMIN && userRepository.existsByRole(Role.ADMIN)) {
	        return ApiResponse.builder()
	                .statusCode(HttpStatus.BAD_REQUEST.value())
	                .message("ADMIN user already exists! Cannot create another ADMIN.")
	                .build();
	    }
		 User user = User.builder()
		            .email(userRequest.getEmail())
		            .username(userRequest.getUsername())
		            .password(passwordEncoder.encode(userRequest.getPassword()))
		            .role(userRequest.getRole())
		            .build();
		    User savedUser = userRepository.save(user);

		    return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
					.message(Constants.USER_CREATED_SUCCESSFULLY).response(savedUser).build();
	}

	@Override
	public UserResponse getUserByUsername(String email) {
		User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
	        return mapToUserResponse(user);
	}
	
	private UserResponse mapToUserResponse(User user) {
	    UserResponse response = new UserResponse();
	    response.setUsername(user.getUsername());
	    response.setEmail(user.getEmail());
	    response.setRole(user.getRole().name()); 
	    return response;
	}

}

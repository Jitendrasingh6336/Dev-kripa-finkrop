package com.example.dev.serviceimpl;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.model.ChangePasswordRequest;
import com.example.dev.model.Lead;
import com.example.dev.model.LeadRemarksRequest;
import com.example.dev.model.Role;
import com.example.dev.model.User;
import com.example.dev.model.UserStatus;
import com.example.dev.repository.UserRepository;
import com.example.dev.request.PaginationRequest;
import com.example.dev.request.UserRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.LeadResponse;
import com.example.dev.response.PaginatedResponse;
import com.example.dev.response.UserCountResponse;
import com.example.dev.response.UserResponse;
import com.example.dev.service.IUserService;
import com.example.dev.util.AppUtil;
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
		            .role(userRequest.getRole()).createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).isActive(Boolean.TRUE)
		            .isDeleted(Boolean.FALSE).status(userRequest.getStatus())
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
		return UserResponse.builder().id(user.getId()).email(user.getEmail()).role(user.getRole()).status(user.getStatus()).username(user.getUsername())
				.build();
	}

	@Override
	public ApiResponse getAllUsers(Integer pageNumber, Integer pageSize, String search) {

	    PaginationRequest pagePaginationRequest = new PaginationRequest();
	    pagePaginationRequest.setPageNumber(pageNumber);
	    pagePaginationRequest.setPageSize(pageSize);

	    Pageable pageableRequest = AppUtil.buildPageableRequest(pagePaginationRequest);

	    Page<User> users = this.userRepository.findUsers(search, pageableRequest);

	    return ApiResponse.builder()
	            .message(Constants.USER_FETCHED)
	            .statusCode(HttpStatus.OK.value())
	            .response(new PaginatedResponse<>(
	                    users.map(this::UserToUserResponse)
	            ))
	            .build();
	}

	private UserResponse UserToUserResponse(User user) {

	    return UserResponse.builder()
	            .id(user.getId())
	            .username(user.getUsername())
	            .email(user.getEmail())
	            .role(user.getRole())
	            .status(user.getStatus())
	            .build();
	}

	@Override
	public ApiResponse deleteUser(String id) {
		// TODO Auto-generated method stub
		User user = this.userRepository.findByIdAndIsDeleted(id, Boolean.FALSE)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));
		user.setIsActive(Boolean.FALSE);
		user.setIsDeleted(Boolean.TRUE);
		this.userRepository.save(user);
		return ApiResponse.builder().message(Constants.USER_DELETE_SUCCESS).statusCode(HttpStatus.OK.value())
				.build();
	}
	
	@Override
	public ApiResponse getUserCounts() {

	    long totalUsers = userRepository.countByIsDeletedFalse();
	    long activeUsers = userRepository.countByIsDeletedFalseAndIsActiveTrue();
	    long inactiveUsers = userRepository.countByIsDeletedFalseAndIsActiveFalse();

	    UserCountResponse response = UserCountResponse.builder()
	            .totalUsers(totalUsers)
	            .activeUsers(activeUsers)
	            .inactiveUsers(inactiveUsers)
	            .build();

	    return ApiResponse.builder()
	            .message("User count fetched successfully")
	            .statusCode(HttpStatus.OK.value())
	            .response(response)
	            .build();
	}
	
	@Override
	public ApiResponse changeUserStatus(String id) {
		User user = this.userRepository.findByIdAndIsDeleted(id, Boolean.FALSE)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));
		user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
		this.userRepository.save(user);
		return ApiResponse.builder().message(Constants.STATUS_CHANGED_SUCCESSFULLY).statusCode(HttpStatus.OK.value()).response(null)
				.build();
	}

	 public ApiResponse changePassword(String email, ChangePasswordRequest request) {

	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        // ✅ Validate old password
	        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
	            throw new ResourceNotFoundException("Old password is incorrect");
	        }

	        // ✅ Prevent same password reuse
	        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
	            throw new ResourceNotFoundException("New password must be different from old password");
	        }

	        // ✅ Encode and update new password
	        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

	        userRepository.save(user);
	        return ApiResponse.builder().message(Constants.PASSWORD_CHANGED_SUCCESSFULLY).statusCode(HttpStatus.OK.value()).response(user)
					.build();
	    }
}

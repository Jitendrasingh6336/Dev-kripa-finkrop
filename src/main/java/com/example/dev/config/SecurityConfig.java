package com.example.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.dev.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	    private final JwtFilter jwtFilter;
	    private final UserDetailsService userDetailsService;
	    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	    
	    public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService, 
	                         JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
	        this.jwtFilter = jwtFilter;
	        this.userDetailsService = userDetailsService;
	        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http
	            .cors(cors -> {})   // ✅ ENABLE CORS
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> auth

	                .requestMatchers(
	                    "/auth/api/v1/login",
	                    "/customer-query/api/v1/create",
	                    "/career/api/v1/create",
	                    "/lead/api/v1/add",
	                    "/faqs/api/v1/get-all-faq",
	                    "/user//api/v1/get-all-users",
                        "/user/api/v1/count"
	                ).permitAll()

	                .requestMatchers(
	                    "/",
	                    "/**/*.html",
	                    "/css/**",
	                    "/js/**",
	                    "/images/**"
	                ).permitAll()

	                .anyRequest().authenticated()
	            )
	            .exceptionHandling(ex -> ex
	                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	            )
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }


	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
	            throws Exception {
	        return config.getAuthenticationManager();
	    }

	    
}

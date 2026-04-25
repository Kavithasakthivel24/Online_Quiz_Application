package com.quizapp.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;
	
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	 @Bean
	 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	     http
	         .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	         .cors(Customizer.withDefaults())
	         .csrf(csrf -> csrf.disable())
	         .authorizeHttpRequests(auth -> auth
	             .requestMatchers(
	                 "/login",
	                 "/doLogin",
	                 "/register",
	                 "/dashboard",
	                 "/create_test",   
	                 "/view_result",
	                 "/saveQuiz",
	                 "/api/**",
	                 "/add-question",
	                 "/quiz",
	                 "/quiz/**",
	                 "/api/quiz/**",
	                 "/saveQuestion",
	                 "/signup",
	                 "/user_dashboard",
	                 "/user_viewresult",
	                 "/Test",
	                 "/start-test",
	                 "/api/**",
	                 "/submitQuiz",
	                 "/signup",
	                 "/forgot-password",
	                 "/forgot-password-page",
	                 "/forgot-password_otp",
	                 "/reset-password",
	                 "/otp-page",
	                 "/reset-password",
	                 "/reset-password-page",
	                 "/api/auth/**",
	                 "/favicon.ico"
	             ).permitAll()
	             .anyRequest().authenticated()
	         )
	         .formLogin(form -> form.disable());

	     return http.build();
	 }
}
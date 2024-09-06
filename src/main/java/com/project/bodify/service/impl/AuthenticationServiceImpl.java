// AuthenticationServiceImpl.java
package com.project.bodify.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bodify.dto.JwtAuthenticationResponse;
import com.project.bodify.dto.SignInRequest;
import com.project.bodify.dto.SignUpRequest;
import com.project.bodify.model.Role;
import com.project.bodify.model.User;
import com.project.bodify.repository.UserRepository;
import com.project.bodify.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            try {
            	Role role;
            	if (request.getRole().equals("COACH")) {
            		role=Role.COACH;
				}else {
					if (request.getRole().equals("ATTENDEE")) {
	            		role=Role.ATTENDEE;
					}else {
						
							role=Role.OWNER;
						}
							
						
					
					
				}
                User user = User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(role)
                        .build();

                user = userRepository.save(user);

                String token = jwtServiceImpl.generateToken(user);

                return JwtAuthenticationResponse.builder().token(token).role(user.getRole().toString()).build();
            } catch (Exception e) {
              
                throw new RuntimeException("Error occurred during signup.", e);
            }
        } else {
            throw new RuntimeException("User with email already exists.");
        }
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

            String token = jwtServiceImpl.generateToken(user);

            return JwtAuthenticationResponse.builder().token(token).role(user.getRole().toString()).build();
        } catch (Exception e) {
         
            throw new RuntimeException("Error occurred during signin.", e);
        }
    }
    
    
}

package com.project.bodify.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bodify.dto.ChangePasswordRequest;
import com.project.bodify.dto.ForgotPasswordRequest;
import com.project.bodify.dto.JwtAuthenticationResponse;
import com.project.bodify.dto.ResetPasswordRequest;
import com.project.bodify.dto.SignInRequest;
import com.project.bodify.dto.SignUpRequest;
import com.project.bodify.dto.UserDto;
import com.project.bodify.model.User;
import com.project.bodify.service.impl.AuthenticationServiceImpl;
import com.project.bodify.service.impl.FilesServiceImpl;
import com.project.bodify.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    private final UserServiceImpl userServiceImpl;
    @Autowired
    private FilesServiceImpl filesServiceImpl;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Validated SignUpRequest request) {
        if (!userServiceImpl.existsByEmail(request.getEmail())) {
            try {
                JwtAuthenticationResponse response = authenticationService.signup(request);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during signup.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email already exists.");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Validated SignInRequest request) {
        try {
            JwtAuthenticationResponse response = authenticationService.signin(request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. Please check your email or sign up.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid email or password.");
        }
    }

    @GetMapping("/me")
    private UserDto me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed or user not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        return new UserDto(
        	    user.getId(), 
        	    user.getFirstName() + " " + user.getLastName(), 
        	    user.getEmail(),
        	    user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate() : null, 
        	    user.getAuthorities().size() > 0 ? user.getAuthorities().iterator().next().toString() : "No Authorities"
        	
);
    }
    
    
    @GetMapping("/user/{id}")
    private UserDto userbyid(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed or user not authenticated");
        }
        User user = userServiceImpl.getUserbyid(id).get();
       return new UserDto(
    		    user.getId(), 
    		    user.getFirstName() + " " + user.getLastName(), 
    		    user.getEmail(),
    		    user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate() : null, 
    		    user.getAuthorities().size() > 0 ? user.getAuthorities().iterator().next().toString() : "No Authorities"
    		
);

    }
    
    
    
    
    
    @GetMapping("/allusers")
    private List<UserDto>  allusers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed or user not authenticated");
        }
        List<UserDto>  alluserdto=new ArrayList<UserDto>();
        List<User> users =userServiceImpl.allusers();
        for (User user : users) {
        	alluserdto.add( 	new UserDto(
        		    user.getId(), 
        		    user.getFirstName() + " " + user.getLastName(), 
        		    user.getEmail(),
        		    user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate() : null, 
        		    user.getAuthorities().size() > 0 ? user.getAuthorities().iterator().next().toString() : "No Authorities"
        		)
);
        	
		}
        
        
        return alluserdto;
    }
    
    
    
    
    
    
    @PutMapping("/update-name")
    public ResponseEntity<User> updateName(@RequestParam String firstName, @RequestParam String lastName) {
        User updatedUser = userServiceImpl.updateUserNames(me().getId(), firstName, lastName);
        return ResponseEntity.ok(updatedUser);
    }
    
    
    
    @PutMapping("/update-photo")
    public ResponseEntity<User> updateName(@RequestParam MultipartFile photo) throws IOException {
        User updatedUser = userServiceImpl.updateUserphoto(me().getId(),filesServiceImpl.storeFileWithGeneratedName(photo));
        return ResponseEntity.ok(updatedUser);
    }
    
    
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    	userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }
    
    
    
    

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Validated ChangePasswordRequest changePasswordRequest) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = userDetails.getUsername();
            userServiceImpl.changePassword(email, changePasswordRequest);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing the password");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Validated ForgotPasswordRequest request) {
        try {
            userServiceImpl.initiateForgotPassword(request);
            return ResponseEntity.ok("Password reset instructions have been sent to your email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the forgot password request");
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Validated ResetPasswordRequest request) {
        try {
            userServiceImpl.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password has been reset successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while resetting the password: " + e.getMessage());
        }
    }
}

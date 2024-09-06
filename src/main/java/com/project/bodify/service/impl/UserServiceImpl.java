package com.project.bodify.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.project.bodify.dto.ChangePasswordRequest;
import com.project.bodify.dto.ForgotPasswordRequest;
import com.project.bodify.model.Files;
import com.project.bodify.model.Role;
import com.project.bodify.model.User;
import com.project.bodify.repository.UserRepository;
import com.project.bodify.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    
    // This method loads user details by username (in this case, email)
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                if (StringUtils.isEmpty(username)) {
                    throw new IllegalArgumentException("Username cannot be null or empty");
                }

                Optional<User> userOptional = userRepository.findByEmail(username);
                if (userOptional.isPresent()) {
                    return userOptional.get();
                } else {
                    throw new UsernameNotFoundException("User not found with username: " + username);
                }
            }
        };
    }

    
    @Transactional
    public User save(User newUser) {
        if (newUser.getId() == null) {
            newUser.setCreatedAt(LocalDateTime.now());
        }

        newUser.setUpdatedAt(LocalDateTime.now());
    
        return userRepository.save(newUser);
    }

 
    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
    
    
   
    public Optional<User> getUserbyid(Long userId) {
        
		return userRepository.findById(userId);
    }

    
  //  @Transactional
    public User update(User updatedUser) {
        if (userRepository.existsById(updatedUser.getId())) {
            updatedUser.setUpdatedAt(LocalDateTime.now());
         
            return userRepository.save(updatedUser);
        } else {
            throw new IllegalArgumentException("User with provided ID does not exist");
        }
    }

    
    
    public User updateUserNames(Long userId, String newFirstName, String newLastName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setFirstName(newFirstName);
        user.setLastName(newLastName);

        return userRepository.save(user);
    }
    
    
    
    public User updateUserphoto(Long userId,Files photo) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPhoto(photo);
      

        return userRepository.save(user);
    }
    
    
 
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));
    }
    
    
    public  List<String> findTokenFcmByRole(Role role){
		return userRepository.findTokenFcmByRole(role);
 
    	
    }
    
    
    public List<User> allusers() {
        return userRepository.findAll();
    }
    
    
    public List<User> findOwner() {
        return userRepository.findByRole(Role.OWNER);
    }
    
    public List<User> findAttendee() {
        return userRepository.findByRole(Role.ATTENDEE);
    }
    
    public List<User> findCoach() {
        return userRepository.findByRole(Role.COACH);
    }
    
   
    

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        userRepository.deleteById(userId);
    }
    
 
// @Transactional
 public User updateTokenFCM(String email, String newTokenFCM) {
     User user = getByEmail(email);
     user.setTokenfcm(newTokenFCM);
     user.setUpdatedAt(LocalDateTime.now()); 
     return userRepository.save(user);
 }

    
    
    

 public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
     User user = userRepository.findByEmail(email)
             .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));

     if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
         throw new IllegalArgumentException("Current password is incorrect");
     }

     user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
     user.setUpdatedAt(LocalDateTime.now());
     userRepository.save(user);
 } 
    
 

 public void initiateForgotPassword(ForgotPasswordRequest request) {
     User user = userRepository.findByEmail(request.getEmail())
             .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + request.getEmail()));

     String token = UUID.randomUUID().toString();
     user.setPasswordResetToken(token);
     user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
     userRepository.save(user);

     emailService.sendPasswordResetEmail(user.getEmail(), token);
 }


 public void resetPassword(String token, String newPassword) {
     User user = userRepository.findByPasswordResetToken(token)
             .orElseThrow(() -> new IllegalArgumentException("Invalid password reset token"));

     if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
         throw new IllegalArgumentException("Password reset token has expired");
     }

     user.setPassword(passwordEncoder.encode(newPassword));
     user.setPasswordResetToken(null);
     user.setPasswordResetTokenExpiry(null);
     user.setUpdatedAt(LocalDateTime.now());
     userRepository.save(user);
 } 
 
 
 
    
    
    
  
}

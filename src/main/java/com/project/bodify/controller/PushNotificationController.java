package com.project.bodify.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bodify.model.PushNotificationRequest;
import com.project.bodify.model.PushNotificationResponse;
import com.project.bodify.model.User;
import com.project.bodify.service.PushNotificationService;
import com.project.bodify.service.impl.UserServiceImpl;


@RestController
public class PushNotificationController {
	@Autowired
private	UserServiceImpl  uServiceImpler;
	@Autowired
    private PushNotificationService pushNotificationService;
    
    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }
    
   /* @PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        System.out.println("princr");
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }*/
    
    
    
    @PutMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestParam String tokenfcm) {
    	uServiceImpler.updateTokenFCM(getemailFromToken(), tokenfcm);
        return new ResponseEntity<>( HttpStatus.OK);
    }
    
    
    
    
    
    private String getemailFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed or user not authenticated");
        }
       User us= (User) authentication.getPrincipal(); 
       return (String)us.getEmail();
    } 
}
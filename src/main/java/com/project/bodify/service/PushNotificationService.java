package com.project.bodify.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.project.bodify.model.PushNotificationRequest;


@Service
public class PushNotificationService {
	
    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    
    private FCMService fcmService;
    
    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }
    
    
    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
   
    
    public void sendPushNotificationToTokens(PushNotificationRequest request, List<String> tokens) {
        try {
            for (String token : tokens) {
                request.setToken(token);
                fcmService.sendMessageToToken(request);
            }
        } catch (Exception e) {
            logger.error("Error sending push notification: {}", e.getMessage());
        }
    }
      
}
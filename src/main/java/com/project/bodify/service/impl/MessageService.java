package com.project.bodify.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bodify.model.Message;
import com.project.bodify.model.PushNotificationRequest;
import com.project.bodify.model.User;
import com.project.bodify.repository.MessageRepository;
import com.project.bodify.service.PushNotificationService;

@Service
public class MessageService {
    
	
	 @Autowired
	    private  PushNotificationService notificationService;
	    @Autowired
	    private  UserServiceImpl userServiceImpl;
	    
    @Autowired
    private MessageRepository messageRepo;
    
    private List<String> recivedmessage;

    // Create a new message
    public Message createMessage(Long toid, Long fromid, String img, String msg) {

        
        User user = userServiceImpl.getUserbyid(toid).orElseThrow();

       
        String tokenfcm = user.getTokenfcm();

       
        if (tokenfcm != null) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            String notificationMessage = "" + fullName +  msg;

            // Send notification using the token
            notificationService.sendPushNotificationToToken(
                new PushNotificationRequest("Message ", notificationMessage, tokenfcm, tokenfcm)
            );
        }

        // Save and return the new message
        return messageRepo.save(new Message(toid.toString(), fromid.toString(), new Date(), img, msg));
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    

    // Retrieve a message by ID
    public Optional<Message> getMessage(Long id) {
        return messageRepo.findById(id);
    }

    // List received messages by user ID
    public List<String> listReceivedMessages(String userId) {
        List<String> receivedMessages = new ArrayList<>(); 
        List<Message> messages = messageRepo.findByFromidOrToid(userId, userId);

        for (Message msg : messages) {
            if (!receivedMessages.contains(msg.getFromid())) {
                receivedMessages.add(msg.getFromid());
            }
            if (!receivedMessages.contains(msg.getToid())) {
                receivedMessages.add(msg.getToid());
            }
        }

        return receivedMessages;
    }

    // Retrieve conversation between two users
    public List<Message> getConversation(String userId, String otherUserId) {
        return messageRepo.findByFromidAndToidOrToidAndFromid(userId, otherUserId);
    }

    // Delete a message by ID
    public boolean deleteMessageById(Long id) {
        if (messageRepo.existsById(id)) {
            messageRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // Mark a message as viewed
    public Message markMessageAsViewed(Long id) {
        if (messageRepo.existsById(id)) {
            Message message = messageRepo.findById(id).orElseThrow();
            message.setView(1);
            return messageRepo.save(message);
        } else {
            return null;
        }
    }
}

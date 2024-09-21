package com.project.bodify.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bodify.model.Message;
import com.project.bodify.service.impl.MessageService;
import com.project.bodify.dto.AccDto;
import com.project.bodify.dto.GetConvDto;
import com.project.bodify.dto.MessageDto;


@RestController
@Validated
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Create a new message
    @PostMapping("/add")
    public ResponseEntity<Message> createMessage(@RequestBody @Validated MessageDto messageDto) {
        return ResponseEntity.ok(messageService.createMessage(
                messageDto.getToid(), messageDto.getFromid(), messageDto.getImg(), messageDto.getMsg()));
    }

    // Get a message by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Message>> getMessage(@PathVariable @Validated Long id) {
        return ResponseEntity.ok(messageService.getMessage(id));
    }

    // List all received messages for a user
    @GetMapping("/chatlist/{userid}")
    public ResponseEntity<List<String>> listReceivedMessages(@PathVariable @Validated String userid) {
        return ResponseEntity.ok(messageService.listReceivedMessages(userid));
    }

    // Get conversation between two users
    @GetMapping("/conversion")
    public ResponseEntity<List<Message>> getConversation(@RequestBody @Validated GetConvDto convDto) {
        return ResponseEntity.ok(messageService.getConversation(convDto.getId(), convDto.getAutre()));
    }

    // Delete a message by ID
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteMessage(@RequestBody @Validated AccDto deleteDto) {
        if (messageService.getMessage(deleteDto.getId()).orElseThrow().getFromid().equals(deleteDto.getUserid())) {
            return ResponseEntity.ok(messageService.deleteMessageById(deleteDto.getId()));
        } else {
            return ResponseEntity.ok(false);
        }
    }

    // Mark a message as viewed
    @PutMapping("/viewed")
    public ResponseEntity<Message> markAsViewed(@RequestBody @Validated AccDto viewDto) {
        if (messageService.getMessage(viewDto.getId()).orElseThrow().getToid().equals(viewDto.getUserid())) {
            return ResponseEntity.ok(messageService.markMessageAsViewed(viewDto.getId()));
        } else {
            return ResponseEntity.ok(null);
        }
    }
}

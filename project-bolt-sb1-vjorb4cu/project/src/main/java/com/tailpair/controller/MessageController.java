package com.tailpair.controller;

import com.tailpair.dto.MessageDto;
import com.tailpair.security.UserPrincipal;
import com.tailpair.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@Valid @RequestBody MessageDto messageDto,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        MessageDto sentMessage = messageService.sendMessage(messageDto, userPrincipal.getId());
        return ResponseEntity.ok(sentMessage);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable Long id) {
        MessageDto message = messageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<MessageDto> markAsRead(@PathVariable Long id,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        MessageDto message = messageService.markAsRead(id, userPrincipal.getId());
        return ResponseEntity.ok(message);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        messageService.deleteMessage(id, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/user")
    public ResponseEntity<Page<MessageDto>> getMessagesByUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              Pageable pageable) {
        Page<MessageDto> messages = messageService.getMessagesByUserId(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/sent")
    public ResponseEntity<Page<MessageDto>> getSentMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            Pageable pageable) {
        Page<MessageDto> messages = messageService.getSentMessages(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/received")
    public ResponseEntity<Page<MessageDto>> getReceivedMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                Pageable pageable) {
        Page<MessageDto> messages = messageService.getReceivedMessages(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/unread")
    public ResponseEntity<Page<MessageDto>> getUnreadMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              Pageable pageable) {
        Page<MessageDto> messages = messageService.getUnreadMessages(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadMessageCount(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long count = messageService.getUnreadMessageCount(userPrincipal.getId());
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<Page<MessageDto>> getMessagesByAnimalAndUser(@PathVariable Long animalId,
                                                                       @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                       Pageable pageable) {
        Page<MessageDto> messages = messageService.getMessagesByAnimalAndUser(animalId, userPrincipal.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
}
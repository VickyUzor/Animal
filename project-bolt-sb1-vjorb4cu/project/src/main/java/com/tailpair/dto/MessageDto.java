package com.tailpair.dto;

import com.tailpair.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class MessageDto {
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String subject;
    
    @NotBlank
    @Size(max = 2000)
    private String content;
    
    private boolean isRead;
    private LocalDateTime createdAt;
    
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;
    private Long animalId;
    private String animalName;

    // Constructors
    public MessageDto() {}

    public MessageDto(Message message) {
        this.id = message.getId();
        this.subject = message.getSubject();
        this.content = message.getContent();
        this.isRead = message.isRead();
        this.createdAt = message.getCreatedAt();
        
        if (message.getSender() != null) {
            this.senderId = message.getSender().getId();
            this.senderName = message.getSender().getFirstName() + " " + message.getSender().getLastName();
        }
        
        if (message.getRecipient() != null) {
            this.recipientId = message.getRecipient().getId();
            this.recipientName = message.getRecipient().getFirstName() + " " + message.getRecipient().getLastName();
        }
        
        if (message.getAnimal() != null) {
            this.animalId = message.getAnimal().getId();
            this.animalName = message.getAnimal().getName();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }

    public String getAnimalName() { return animalName; }
    public void setAnimalName(String animalName) { this.animalName = animalName; }
}
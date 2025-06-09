package com.tailpair.dto;

import com.tailpair.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @NotBlank
    @Size(max = 500)
    private String message;
    
    private Notification.Type type;
    private boolean isRead;
    private LocalDateTime createdAt;
    
    private Long userId;
    private Long animalId;
    private String animalName;

    // Constructors
    public NotificationDto() {}

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.type = notification.getType();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
        
        if (notification.getUser() != null) {
            this.userId = notification.getUser().getId();
        }
        
        if (notification.getAnimal() != null) {
            this.animalId = notification.getAnimal().getId();
            this.animalName = notification.getAnimal().getName();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Notification.Type getType() { return type; }
    public void setType(Notification.Type type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }

    public String getAnimalName() { return animalName; }
    public void setAnimalName(String animalName) { this.animalName = animalName; }
}
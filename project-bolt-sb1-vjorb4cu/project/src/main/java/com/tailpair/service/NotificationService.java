package com.tailpair.service;

import com.tailpair.dto.NotificationDto;
import com.tailpair.entity.*;
import com.tailpair.exception.ResourceNotFoundException;
import com.tailpair.repository.NotificationRepository;
import com.tailpair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public NotificationDto createNotification(NotificationDto notificationDto, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Notification notification = new Notification();
        notification.setTitle(notificationDto.getTitle());
        notification.setMessage(notificationDto.getMessage());
        notification.setType(notificationDto.getType());
        notification.setUser(user);
        
        Notification savedNotification = notificationRepository.save(notification);
        return new NotificationDto(savedNotification);
    }
    
    public void createAdoptionRequestNotification(User shelterAdmin, User adopter, Animal animal) {
        Notification notification = new Notification();
        notification.setTitle("New Adoption Request");
        notification.setMessage(String.format("%s %s has requested to adopt %s", 
            adopter.getFirstName(), adopter.getLastName(), animal.getName()));
        notification.setType(Notification.Type.ADOPTION_REQUEST);
        notification.setUser(shelterAdmin);
        notification.setAnimal(animal);
        
        notificationRepository.save(notification);
    }
    
    public void createAdoptionApprovedNotification(User adopter, Animal animal) {
        Notification notification = new Notification();
        notification.setTitle("Adoption Request Approved");
        notification.setMessage(String.format("Your adoption request for %s has been approved!", animal.getName()));
        notification.setType(Notification.Type.ADOPTION_APPROVED);
        notification.setUser(adopter);
        notification.setAnimal(animal);
        
        notificationRepository.save(notification);
    }
    
    public void createAdoptionRejectedNotification(User adopter, Animal animal, String reason) {
        Notification notification = new Notification();
        notification.setTitle("Adoption Request Rejected");
        notification.setMessage(String.format("Your adoption request for %s has been rejected. Reason: %s", 
            animal.getName(), reason));
        notification.setType(Notification.Type.ADOPTION_REJECTED);
        notification.setUser(adopter);
        notification.setAnimal(animal);
        
        notificationRepository.save(notification);
    }
    
    public void createMessageNotification(User recipient, User sender, Message message) {
        Notification notification = new Notification();
        notification.setTitle("New Message");
        notification.setMessage(String.format("You have a new message from %s %s: %s", 
            sender.getFirstName(), sender.getLastName(), message.getSubject()));
        notification.setType(Notification.Type.MESSAGE_RECEIVED);
        notification.setUser(recipient);
        if (message.getAnimal() != null) {
            notification.setAnimal(message.getAnimal());
        }
        
        notificationRepository.save(notification);
    }
    
    public void createFavoriteAdoptedNotification(User user, Animal animal) {
        Notification notification = new Notification();
        notification.setTitle("Favorite Animal Adopted");
        notification.setMessage(String.format("Your favorite animal %s has been adopted by someone else.", 
            animal.getName()));
        notification.setType(Notification.Type.FAVORITE_ADOPTED);
        notification.setUser(user);
        notification.setAnimal(animal);
        
        notificationRepository.save(notification);
    }
    
    public NotificationDto getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return new NotificationDto(notification);
    }
    
    public NotificationDto markAsRead(Long id, Long userId) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        
        if (!notification.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only mark your own notifications as read");
        }
        
        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return new NotificationDto(updatedNotification);
    }
    
    public void deleteNotification(Long id, Long userId) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        
        if (!notification.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only delete your own notifications");
        }
        
        notificationRepository.deleteById(id);
    }
    
    public Page<NotificationDto> getNotificationsByUserId(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
            .map(NotificationDto::new);
    }
    
    public Page<NotificationDto> getUnreadNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findUnreadNotificationsByUserId(userId, pageable)
            .map(NotificationDto::new);
    }
    
    public long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countUnreadNotificationsByUserId(userId);
    }
    
    public Page<NotificationDto> getNotificationsByType(Long userId, Notification.Type type, Pageable pageable) {
        return notificationRepository.findByUserIdAndType(userId, type, pageable)
            .map(NotificationDto::new);
    }
}
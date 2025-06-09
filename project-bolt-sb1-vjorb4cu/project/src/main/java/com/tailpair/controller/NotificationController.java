package com.tailpair.controller;

import com.tailpair.dto.NotificationDto;
import com.tailpair.entity.Notification;
import com.tailpair.security.UserPrincipal;
import com.tailpair.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationDto> createNotification(@Valid @RequestBody NotificationDto notificationDto,
                                                              @RequestParam Long userId) {
        NotificationDto createdNotification = notificationService.createNotification(notificationDto, userId);
        return ResponseEntity.ok(createdNotification);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Long id) {
        NotificationDto notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        NotificationDto notification = notificationService.markAsRead(id, userPrincipal.getId());
        return ResponseEntity.ok(notification);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        notificationService.deleteNotification(id, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/user")
    public ResponseEntity<Page<NotificationDto>> getNotificationsByUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                        Pageable pageable) {
        Page<NotificationDto> notifications = notificationService.getNotificationsByUserId(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread")
    public ResponseEntity<Page<NotificationDto>> getUnreadNotifications(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                        Pageable pageable) {
        Page<NotificationDto> notifications = notificationService.getUnreadNotifications(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadNotificationCount(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long count = notificationService.getUnreadNotificationCount(userPrincipal.getId());
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<Page<NotificationDto>> getNotificationsByType(@PathVariable Notification.Type type,
                                                                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                        Pageable pageable) {
        Page<NotificationDto> notifications = notificationService.getNotificationsByType(userPrincipal.getId(), type, pageable);
        return ResponseEntity.ok(notifications);
    }
}
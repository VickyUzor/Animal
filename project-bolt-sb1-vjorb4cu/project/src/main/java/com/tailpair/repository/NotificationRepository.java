package com.tailpair.repository;

import com.tailpair.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    @Query("SELECT n FROM Notification n WHERE " +
           "n.user.id = :userId AND n.isRead = false " +
           "ORDER BY n.createdAt DESC")
    Page<Notification> findUnreadNotificationsByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE " +
           "n.user.id = :userId AND n.isRead = false")
    long countUnreadNotificationsByUserId(@Param("userId") Long userId);
    
    Page<Notification> findByUserIdAndType(Long userId, Notification.Type type, Pageable pageable);
}
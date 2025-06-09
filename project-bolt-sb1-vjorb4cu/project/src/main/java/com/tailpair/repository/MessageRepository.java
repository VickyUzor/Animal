package com.tailpair.repository;

import com.tailpair.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findBySenderId(Long senderId, Pageable pageable);
    Page<Message> findByRecipientId(Long recipientId, Pageable pageable);
    
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId OR m.recipient.id = :userId)")
    Page<Message> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT m FROM Message m WHERE " +
           "m.recipient.id = :userId AND m.isRead = false")
    Page<Message> findUnreadMessagesByRecipientId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE " +
           "m.recipient.id = :userId AND m.isRead = false")
    long countUnreadMessagesByRecipientId(@Param("userId") Long userId);
    
    @Query("SELECT m FROM Message m WHERE " +
           "m.animal.id = :animalId AND " +
           "(m.sender.id = :userId OR m.recipient.id = :userId)")
    Page<Message> findByAnimalIdAndUserId(@Param("animalId") Long animalId, @Param("userId") Long userId, Pageable pageable);
}
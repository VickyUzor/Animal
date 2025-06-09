package com.tailpair.service;

import com.tailpair.dto.MessageDto;
import com.tailpair.entity.Animal;
import com.tailpair.entity.Message;
import com.tailpair.entity.User;
import com.tailpair.exception.ResourceNotFoundException;
import com.tailpair.repository.AnimalRepository;
import com.tailpair.repository.MessageRepository;
import com.tailpair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AnimalRepository animalRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    public MessageDto sendMessage(MessageDto messageDto, Long senderId) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new ResourceNotFoundException("Sender not found with id: " + senderId));
        
        User recipient = userRepository.findById(messageDto.getRecipientId())
            .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + messageDto.getRecipientId()));
        
        Message message = new Message();
        message.setSubject(messageDto.getSubject());
        message.setContent(messageDto.getContent());
        message.setSender(sender);
        message.setRecipient(recipient);
        
        if (messageDto.getAnimalId() != null) {
            Animal animal = animalRepository.findById(messageDto.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + messageDto.getAnimalId()));
            message.setAnimal(animal);
        }
        
        Message savedMessage = messageRepository.save(message);
        
        // Send notification to recipient
        notificationService.createMessageNotification(recipient, sender, savedMessage);
        
        return new MessageDto(savedMessage);
    }
    
    public MessageDto getMessageById(Long id) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        return new MessageDto(message);
    }
    
    public MessageDto markAsRead(Long id, Long userId) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        
        // Only the recipient can mark the message as read
        if (!message.getRecipient().getId().equals(userId)) {
            throw new IllegalArgumentException("Only the recipient can mark the message as read");
        }
        
        message.setRead(true);
        Message updatedMessage = messageRepository.save(message);
        return new MessageDto(updatedMessage);
    }
    
    public void deleteMessage(Long id, Long userId) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        
        // Only sender or recipient can delete the message
        if (!message.getSender().getId().equals(userId) && 
            !message.getRecipient().getId().equals(userId)) {
            throw new IllegalArgumentException("Only sender or recipient can delete the message");
        }
        
        messageRepository.deleteById(id);
    }
    
    public Page<MessageDto> getMessagesByUserId(Long userId, Pageable pageable) {
        return messageRepository.findByUserId(userId, pageable)
            .map(MessageDto::new);
    }
    
    public Page<MessageDto> getSentMessages(Long userId, Pageable pageable) {
        return messageRepository.findBySenderId(userId, pageable)
            .map(MessageDto::new);
    }
    
    public Page<MessageDto> getReceivedMessages(Long userId, Pageable pageable) {
        return messageRepository.findByRecipientId(userId, pageable)
            .map(MessageDto::new);
    }
    
    public Page<MessageDto> getUnreadMessages(Long userId, Pageable pageable) {
        return messageRepository.findUnreadMessagesByRecipientId(userId, pageable)
            .map(MessageDto::new);
    }
    
    public long getUnreadMessageCount(Long userId) {
        return messageRepository.countUnreadMessagesByRecipientId(userId);
    }
    
    public Page<MessageDto> getMessagesByAnimalAndUser(Long animalId, Long userId, Pageable pageable) {
        return messageRepository.findByAnimalIdAndUserId(animalId, userId, pageable)
            .map(MessageDto::new);
    }
}
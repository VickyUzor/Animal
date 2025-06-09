package com.tailpair.service;

import com.tailpair.dto.AdoptionDto;
import com.tailpair.entity.Adoption;
import com.tailpair.entity.Animal;
import com.tailpair.entity.User;
import com.tailpair.exception.ResourceNotFoundException;
import com.tailpair.repository.AdoptionRepository;
import com.tailpair.repository.AnimalRepository;
import com.tailpair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AdoptionService {
    
    @Autowired
    private AdoptionRepository adoptionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AnimalRepository animalRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    public AdoptionDto createAdoptionRequest(Long adopterId, Long animalId, String notes) {
        User adopter = userRepository.findById(adopterId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + adopterId));
        
        Animal animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + animalId));
        
        if (animal.getStatus() != Animal.Status.AVAILABLE) {
            throw new IllegalArgumentException("Animal is not available for adoption");
        }
        
        if (adoptionRepository.existsByAdopterIdAndAnimalId(adopterId, animalId)) {
            throw new IllegalArgumentException("Adoption request already exists for this animal");
        }
        
        Adoption adoption = new Adoption(adopter, animal);
        adoption.setNotes(notes);
        
        Adoption savedAdoption = adoptionRepository.save(adoption);
        
        // Update animal status to pending
        animal.setStatus(Animal.Status.PENDING);
        animalRepository.save(animal);
        
        // Notify shelter admin
        if (animal.getShelter().getAdmin() != null) {
            notificationService.createAdoptionRequestNotification(
                animal.getShelter().getAdmin(), adopter, animal);
        }
        
        return new AdoptionDto(savedAdoption);
    }
    
    public AdoptionDto approveAdoption(Long adoptionId, Long shelterId) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption not found with id: " + adoptionId));
        
        if (!adoption.getAnimal().getShelter().getId().equals(shelterId)) {
            throw new IllegalArgumentException("You can only approve adoptions for your shelter");
        }
        
        if (adoption.getStatus() != Adoption.Status.PENDING) {
            throw new IllegalArgumentException("Adoption is not in pending status");
        }
        
        adoption.setStatus(Adoption.Status.APPROVED);
        adoption.setApprovedAt(LocalDateTime.now());
        
        Adoption updatedAdoption = adoptionRepository.save(adoption);
        
        // Notify adopter
        notificationService.createAdoptionApprovedNotification(
            adoption.getAdopter(), adoption.getAnimal());
        
        return new AdoptionDto(updatedAdoption);
    }
    
    public AdoptionDto rejectAdoption(Long adoptionId, Long shelterId, String rejectionReason) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption not found with id: " + adoptionId));
        
        if (!adoption.getAnimal().getShelter().getId().equals(shelterId)) {
            throw new IllegalArgumentException("You can only reject adoptions for your shelter");
        }
        
        if (adoption.getStatus() != Adoption.Status.PENDING) {
            throw new IllegalArgumentException("Adoption is not in pending status");
        }
        
        adoption.setStatus(Adoption.Status.REJECTED);
        adoption.setRejectionReason(rejectionReason);
        
        // Make animal available again
        Animal animal = adoption.getAnimal();
        animal.setStatus(Animal.Status.AVAILABLE);
        animalRepository.save(animal);
        
        Adoption updatedAdoption = adoptionRepository.save(adoption);
        
        // Notify adopter
        notificationService.createAdoptionRejectedNotification(
            adoption.getAdopter(), adoption.getAnimal(), rejectionReason);
        
        return new AdoptionDto(updatedAdoption);
    }
    
    public AdoptionDto completeAdoption(Long adoptionId, Long shelterId) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption not found with id: " + adoptionId));
        
        if (!adoption.getAnimal().getShelter().getId().equals(shelterId)) {
            throw new IllegalArgumentException("You can only complete adoptions for your shelter");
        }
        
        if (adoption.getStatus() != Adoption.Status.APPROVED) {
            throw new IllegalArgumentException("Adoption must be approved before completion");
        }
        
        adoption.setStatus(Adoption.Status.COMPLETED);
        adoption.setCompletedAt(LocalDateTime.now());
        
        // Mark animal as adopted
        Animal animal = adoption.getAnimal();
        animal.setStatus(Animal.Status.ADOPTED);
        animalRepository.save(animal);
        
        Adoption updatedAdoption = adoptionRepository.save(adoption);
        
        return new AdoptionDto(updatedAdoption);
    }
    
    public AdoptionDto getAdoptionById(Long id) {
        Adoption adoption = adoptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption not found with id: " + id));
        return new AdoptionDto(adoption);
    }
    
    public void cancelAdoption(Long adoptionId, Long userId) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption not found with id: " + adoptionId));
        
        if (!adoption.getAdopter().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only cancel your own adoption requests");
        }
        
        if (adoption.getStatus() == Adoption.Status.COMPLETED) {
            throw new IllegalArgumentException("Cannot cancel completed adoption");
        }
        
        adoption.setStatus(Adoption.Status.CANCELLED);
        
        // Make animal available again if it was pending
        if (adoption.getAnimal().getStatus() == Animal.Status.PENDING) {
            Animal animal = adoption.getAnimal();
            animal.setStatus(Animal.Status.AVAILABLE);
            animalRepository.save(animal);
        }
        
        adoptionRepository.save(adoption);
    }
    
    public Page<AdoptionDto> getAdoptionsByAdopterId(Long adopterId, Pageable pageable) {
        return adoptionRepository.findByAdopterId(adopterId, pageable)
            .map(AdoptionDto::new);
    }
    
    public Page<AdoptionDto> getAdoptionsByShelterId(Long shelterId, Pageable pageable) {
        return adoptionRepository.findByShelterId(shelterId, pageable)
            .map(AdoptionDto::new);
    }
    
    public Page<AdoptionDto> getPendingAdoptionsByShelterId(Long shelterId, Pageable pageable) {
        return adoptionRepository.findByShelterIdAndStatus(shelterId, Adoption.Status.PENDING, pageable)
            .map(AdoptionDto::new);
    }
    
    public long getPendingAdoptionsCountByShelterId(Long shelterId) {
        return adoptionRepository.countPendingAdoptionsByShelterId(shelterId);
    }
    
    public Page<AdoptionDto> getAdoptionsByStatus(Adoption.Status status, Pageable pageable) {
        return adoptionRepository.findByStatus(status, pageable)
            .map(AdoptionDto::new);
    }
}
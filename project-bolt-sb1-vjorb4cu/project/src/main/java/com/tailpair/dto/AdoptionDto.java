package com.tailpair.dto;

import com.tailpair.entity.Adoption;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class AdoptionDto {
    private Long id;
    private Adoption.Status status;
    
    @Size(max = 1000)
    private String notes;
    
    @Size(max = 500)
    private String rejectionReason;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime completedAt;
    
    private Long adopterId;
    private String adopterName;
    private String adopterEmail;
    private Long animalId;
    private String animalName;
    private Long shelterId;
    private String shelterName;

    // Constructors
    public AdoptionDto() {}

    public AdoptionDto(Adoption adoption) {
        this.id = adoption.getId();
        this.status = adoption.getStatus();
        this.notes = adoption.getNotes();
        this.rejectionReason = adoption.getRejectionReason();
        this.createdAt = adoption.getCreatedAt();
        this.updatedAt = adoption.getUpdatedAt();
        this.approvedAt = adoption.getApprovedAt();
        this.completedAt = adoption.getCompletedAt();
        
        if (adoption.getAdopter() != null) {
            this.adopterId = adoption.getAdopter().getId();
            this.adopterName = adoption.getAdopter().getFirstName() + " " + adoption.getAdopter().getLastName();
            this.adopterEmail = adoption.getAdopter().getEmail();
        }
        
        if (adoption.getAnimal() != null) {
            this.animalId = adoption.getAnimal().getId();
            this.animalName = adoption.getAnimal().getName();
            
            if (adoption.getAnimal().getShelter() != null) {
                this.shelterId = adoption.getAnimal().getShelter().getId();
                this.shelterName = adoption.getAnimal().getShelter().getName();
            }
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Adoption.Status getStatus() { return status; }
    public void setStatus(Adoption.Status status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public Long getAdopterId() { return adopterId; }
    public void setAdopterId(Long adopterId) { this.adopterId = adopterId; }

    public String getAdopterName() { return adopterName; }
    public void setAdopterName(String adopterName) { this.adopterName = adopterName; }

    public String getAdopterEmail() { return adopterEmail; }
    public void setAdopterEmail(String adopterEmail) { this.adopterEmail = adopterEmail; }

    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }

    public String getAnimalName() { return animalName; }
    public void setAnimalName(String animalName) { this.animalName = animalName; }

    public Long getShelterId() { return shelterId; }
    public void setShelterId(Long shelterId) { this.shelterId = shelterId; }

    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }
}
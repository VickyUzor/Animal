package com.tailpair.dto;

import com.tailpair.entity.Animal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class AnimalDto {
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    private String name;
    
    @NotNull
    private Animal.Species species;
    
    @Size(max = 50)
    private String breed;
    
    @Positive
    private Integer age;
    
    @NotNull
    private Animal.Gender gender;
    
    @NotNull
    private Animal.Size size;
    
    private BigDecimal weight;
    
    @Size(max = 50)
    private String color;
    
    @Size(max = 1000)
    private String description;
    
    @Size(max = 500)
    private String medicalHistory;
    
    private boolean vaccinated;
    private boolean spayedNeutered;
    private boolean houseTrained;
    private boolean goodWithKids;
    private boolean goodWithPets;
    
    @NotNull
    private Animal.Status status;
    
    @Positive
    private BigDecimal adoptionFee;
    
    private Set<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Long shelterId;
    private String shelterName;
    private boolean isFavorited;

    // Constructors
    public AnimalDto() {}

    public AnimalDto(Animal animal) {
        this.id = animal.getId();
        this.name = animal.getName();
        this.species = animal.getSpecies();
        this.breed = animal.getBreed();
        this.age = animal.getAge();
        this.gender = animal.getGender();
        this.size = animal.getSize();
        this.weight = animal.getWeight();
        this.color = animal.getColor();
        this.description = animal.getDescription();
        this.medicalHistory = animal.getMedicalHistory();
        this.vaccinated = animal.isVaccinated();
        this.spayedNeutered = animal.isSpayedNeutered();
        this.houseTrained = animal.isHouseTrained();
        this.goodWithKids = animal.isGoodWithKids();
        this.goodWithPets = animal.isGoodWithPets();
        this.status = animal.getStatus();
        this.adoptionFee = animal.getAdoptionFee();
        this.imageUrls = animal.getImageUrls();
        this.createdAt = animal.getCreatedAt();
        this.updatedAt = animal.getUpdatedAt();
        
        if (animal.getShelter() != null) {
            this.shelterId = animal.getShelter().getId();
            this.shelterName = animal.getShelter().getName();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Animal.Species getSpecies() { return species; }
    public void setSpecies(Animal.Species species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Animal.Gender getGender() { return gender; }
    public void setGender(Animal.Gender gender) { this.gender = gender; }

    public Animal.Size getSize() { return size; }
    public void setSize(Animal.Size size) { this.size = size; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public boolean isVaccinated() { return vaccinated; }
    public void setVaccinated(boolean vaccinated) { this.vaccinated = vaccinated; }

    public boolean isSpayedNeutered() { return spayedNeutered; }
    public void setSpayedNeutered(boolean spayedNeutered) { this.spayedNeutered = spayedNeutered; }

    public boolean isHouseTrained() { return houseTrained; }
    public void setHouseTrained(boolean houseTrained) { this.houseTrained = houseTrained; }

    public boolean isGoodWithKids() { return goodWithKids; }
    public void setGoodWithKids(boolean goodWithKids) { this.goodWithKids = goodWithKids; }

    public boolean isGoodWithPets() { return goodWithPets; }
    public void setGoodWithPets(boolean goodWithPets) { this.goodWithPets = goodWithPets; }

    public Animal.Status getStatus() { return status; }
    public void setStatus(Animal.Status status) { this.status = status; }

    public BigDecimal getAdoptionFee() { return adoptionFee; }
    public void setAdoptionFee(BigDecimal adoptionFee) { this.adoptionFee = adoptionFee; }

    public Set<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(Set<String> imageUrls) { this.imageUrls = imageUrls; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getShelterId() { return shelterId; }
    public void setShelterId(Long shelterId) { this.shelterId = shelterId; }

    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }

    public boolean isFavorited() { return isFavorited; }
    public void setFavorited(boolean favorited) { isFavorited = favorited; }
}
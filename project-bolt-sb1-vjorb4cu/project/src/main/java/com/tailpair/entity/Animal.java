package com.tailpair.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "animals")
@EntityListeners(AuditingEntityListener.class)
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Species species;

    @Size(max = 50)
    private String breed;

    @Positive
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Size size;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Size(max = 50)
    private String color;

    @Size(max = 1000)
    private String description;

    @Size(max = 500)
    private String medicalHistory;

    private boolean vaccinated = false;
    private boolean spayedNeutered = false;
    private boolean houseTrained = false;
    private boolean goodWithKids = false;
    private boolean goodWithPets = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    @Positive
    private BigDecimal adoptionFee;

    @ElementCollection
    @CollectionTable(name = "animal_images", joinColumns = @JoinColumn(name = "animal_id"))
    @Column(name = "image_url")
    private Set<String> imageUrls = new HashSet<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Favorite> favorites = new HashSet<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Adoption> adoptions = new HashSet<>();

    public enum Species {
        DOG, CAT, RABBIT, BIRD, HAMSTER, GUINEA_PIG, FERRET, OTHER
    }

    public enum Gender {
        MALE, FEMALE
    }

    public enum Size {
        SMALL, MEDIUM, LARGE, EXTRA_LARGE
    }

    public enum Status {
        AVAILABLE, PENDING, ADOPTED, NOT_AVAILABLE
    }

    // Constructors
    public Animal() {}

    public Animal(String name, Species species, Gender gender, Size size, Shelter shelter) {
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.size = size;
        this.shelter = shelter;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Species getSpecies() { return species; }
    public void setSpecies(Species species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }

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

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public BigDecimal getAdoptionFee() { return adoptionFee; }
    public void setAdoptionFee(BigDecimal adoptionFee) { this.adoptionFee = adoptionFee; }

    public Set<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(Set<String> imageUrls) { this.imageUrls = imageUrls; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Shelter getShelter() { return shelter; }
    public void setShelter(Shelter shelter) { this.shelter = shelter; }

    public Set<Favorite> getFavorites() { return favorites; }
    public void setFavorites(Set<Favorite> favorites) { this.favorites = favorites; }

    public Set<Adoption> getAdoptions() { return adoptions; }
    public void setAdoptions(Set<Adoption> adoptions) { this.adoptions = adoptions; }
}
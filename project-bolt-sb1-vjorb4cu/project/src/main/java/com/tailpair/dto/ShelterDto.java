package com.tailpair.dto;

import com.tailpair.entity.Shelter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ShelterDto {
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    @NotBlank
    @Size(max = 200)
    private String address;
    
    @NotBlank
    @Size(max = 50)
    private String city;
    
    @NotBlank
    @Size(max = 50)
    private String state;
    
    @NotBlank
    @Size(max = 10)
    private String zipCode;
    
    @Size(max = 15)
    private String phone;
    
    @Email
    @Size(max = 100)
    private String email;
    
    @Size(max = 200)
    private String website;
    
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Long adminId;
    private String adminName;
    private int animalCount;

    // Constructors
    public ShelterDto() {}

    public ShelterDto(Shelter shelter) {
        this.id = shelter.getId();
        this.name = shelter.getName();
        this.description = shelter.getDescription();
        this.address = shelter.getAddress();
        this.city = shelter.getCity();
        this.state = shelter.getState();
        this.zipCode = shelter.getZipCode();
        this.phone = shelter.getPhone();
        this.email = shelter.getEmail();
        this.website = shelter.getWebsite();
        this.verified = shelter.isVerified();
        this.createdAt = shelter.getCreatedAt();
        this.updatedAt = shelter.getUpdatedAt();
        
        if (shelter.getAdmin() != null) {
            this.adminId = shelter.getAdmin().getId();
            this.adminName = shelter.getAdmin().getFirstName() + " " + shelter.getAdmin().getLastName();
        }
        
        this.animalCount = shelter.getAnimals().size();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public int getAnimalCount() { return animalCount; }
    public void setAnimalCount(int animalCount) { this.animalCount = animalCount; }
}
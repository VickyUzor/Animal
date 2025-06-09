package com.tailpair.controller;

import com.tailpair.dto.ShelterDto;
import com.tailpair.service.ShelterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelters")
@CrossOrigin(origins = "*")
public class ShelterController {
    
    @Autowired
    private ShelterService shelterService;
    
    @PostMapping
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ShelterDto> createShelter(@Valid @RequestBody ShelterDto shelterDto, 
                                                    @RequestParam Long adminId) {
        ShelterDto createdShelter = shelterService.createShelter(shelterDto, adminId);
        return ResponseEntity.ok(createdShelter);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ShelterDto> getShelterById(@PathVariable Long id) {
        ShelterDto shelter = shelterService.getShelterById(id);
        return ResponseEntity.ok(shelter);
    }
    
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ShelterDto> getShelterByAdminId(@PathVariable Long adminId) {
        ShelterDto shelter = shelterService.getShelterByAdminId(adminId);
        return ResponseEntity.ok(shelter);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ShelterDto> updateShelter(@PathVariable Long id, 
                                                    @Valid @RequestBody ShelterDto shelterDto) {
        ShelterDto updatedShelter = shelterService.updateShelter(id, shelterDto);
        return ResponseEntity.ok(updatedShelter);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<Page<ShelterDto>> getAllShelters(Pageable pageable) {
        Page<ShelterDto> shelters = shelterService.getAllShelters(pageable);
        return ResponseEntity.ok(shelters);
    }
    
    @GetMapping("/verified")
    public ResponseEntity<Page<ShelterDto>> getVerifiedShelters(Pageable pageable) {
        Page<ShelterDto> shelters = shelterService.getVerifiedShelters(pageable);
        return ResponseEntity.ok(shelters);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ShelterDto>> searchShelters(@RequestParam String q, Pageable pageable) {
        Page<ShelterDto> shelters = shelterService.searchShelters(q, pageable);
        return ResponseEntity.ok(shelters);
    }
    
    @GetMapping("/location")
    public ResponseEntity<List<ShelterDto>> getSheltersByLocation(@RequestParam String city, 
                                                                  @RequestParam String state) {
        List<ShelterDto> shelters = shelterService.getSheltersByLocation(city, state);
        return ResponseEntity.ok(shelters);
    }
    
    @GetMapping("/zipcode/{zipCode}")
    public ResponseEntity<List<ShelterDto>> getSheltersByZipCode(@PathVariable String zipCode) {
        List<ShelterDto> shelters = shelterService.getSheltersByZipCode(zipCode);
        return ResponseEntity.ok(shelters);
    }
    
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShelterDto> verifyShelter(@PathVariable Long id) {
        ShelterDto shelter = shelterService.verifyShelter(id);
        return ResponseEntity.ok(shelter);
    }
}
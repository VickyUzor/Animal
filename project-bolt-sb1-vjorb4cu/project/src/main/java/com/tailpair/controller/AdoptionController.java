package com.tailpair.controller;

import com.tailpair.dto.AdoptionDto;
import com.tailpair.entity.Adoption;
import com.tailpair.security.UserPrincipal;
import com.tailpair.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adoptions")
@CrossOrigin(origins = "*")
public class AdoptionController {
    
    @Autowired
    private AdoptionService adoptionService;
    
    @PostMapping
    public ResponseEntity<AdoptionDto> createAdoptionRequest(@RequestParam Long animalId,
                                                             @RequestParam(required = false) String notes,
                                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        AdoptionDto adoption = adoptionService.createAdoptionRequest(userPrincipal.getId(), animalId, notes);
        return ResponseEntity.ok(adoption);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionDto> getAdoptionById(@PathVariable Long id) {
        AdoptionDto adoption = adoptionService.getAdoptionById(id);
        return ResponseEntity.ok(adoption);
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<AdoptionDto> approveAdoption(@PathVariable Long id,
                                                       @RequestParam Long shelterId) {
        AdoptionDto adoption = adoptionService.approveAdoption(id, shelterId);
        return ResponseEntity.ok(adoption);
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<AdoptionDto> rejectAdoption(@PathVariable Long id,
                                                      @RequestParam Long shelterId,
                                                      @RequestParam String rejectionReason) {
        AdoptionDto adoption = adoptionService.rejectAdoption(id, shelterId, rejectionReason);
        return ResponseEntity.ok(adoption);
    }
    
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<AdoptionDto> completeAdoption(@PathVariable Long id,
                                                        @RequestParam Long shelterId) {
        AdoptionDto adoption = adoptionService.completeAdoption(id, shelterId);
        return ResponseEntity.ok(adoption);
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAdoption(@PathVariable Long id,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        adoptionService.cancelAdoption(id, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/user")
    public ResponseEntity<Page<AdoptionDto>> getAdoptionsByUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                Pageable pageable) {
        Page<AdoptionDto> adoptions = adoptionService.getAdoptionsByAdopterId(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(adoptions);
    }
    
    @GetMapping("/shelter/{shelterId}")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Page<AdoptionDto>> getAdoptionsByShelterId(@PathVariable Long shelterId,
                                                                     Pageable pageable) {
        Page<AdoptionDto> adoptions = adoptionService.getAdoptionsByShelterId(shelterId, pageable);
        return ResponseEntity.ok(adoptions);
    }
    
    @GetMapping("/shelter/{shelterId}/pending")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Page<AdoptionDto>> getPendingAdoptionsByShelterId(@PathVariable Long shelterId,
                                                                            Pageable pageable) {
        Page<AdoptionDto> adoptions = adoptionService.getPendingAdoptionsByShelterId(shelterId, pageable);
        return ResponseEntity.ok(adoptions);
    }
    
    @GetMapping("/shelter/{shelterId}/pending/count")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Long> getPendingAdoptionsCountByShelterId(@PathVariable Long shelterId) {
        long count = adoptionService.getPendingAdoptionsCountByShelterId(shelterId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdoptionDto>> getAdoptionsByStatus(@PathVariable Adoption.Status status,
                                                                  Pageable pageable) {
        Page<AdoptionDto> adoptions = adoptionService.getAdoptionsByStatus(status, pageable);
        return ResponseEntity.ok(adoptions);
    }
}
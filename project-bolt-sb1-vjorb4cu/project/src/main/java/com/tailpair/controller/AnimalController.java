package com.tailpair.controller;

import com.tailpair.dto.AnimalDto;
import com.tailpair.entity.Animal;
import com.tailpair.security.UserPrincipal;
import com.tailpair.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
@CrossOrigin(origins = "*")
public class AnimalController {
    
    @Autowired
    private AnimalService animalService;
    
    @PostMapping
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<AnimalDto> createAnimal(@Valid @RequestBody AnimalDto animalDto, 
                                                  @RequestParam Long shelterId) {
        AnimalDto createdAnimal = animalService.createAnimal(animalDto, shelterId);
        return ResponseEntity.ok(createdAnimal);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> getAnimalById(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        AnimalDto animal = animalService.getAnimalById(id, userId);
        return ResponseEntity.ok(animal);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<AnimalDto> updateAnimal(@PathVariable Long id, 
                                                  @Valid @RequestBody AnimalDto animalDto) {
        AnimalDto updatedAnimal = animalService.updateAnimal(id, animalDto);
        return ResponseEntity.ok(updatedAnimal);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/available")
    public ResponseEntity<Page<AnimalDto>> getAvailableAnimals(Pageable pageable,
                                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        Page<AnimalDto> animals = animalService.getAvailableAnimals(pageable, userId);
        return ResponseEntity.ok(animals);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<AnimalDto>> searchAnimals(@RequestParam String q, Pageable pageable) {
        Page<AnimalDto> animals = animalService.searchAnimals(q, pageable);
        return ResponseEntity.ok(animals);
    }
    
    @GetMapping("/filter")
    public ResponseEntity<Page<AnimalDto>> filterAnimals(
            @RequestParam(required = false) Animal.Species species,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Animal.Size size,
            @RequestParam(required = false) Animal.Gender gender,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Boolean goodWithKids,
            @RequestParam(required = false) Boolean goodWithPets,
            @RequestParam(required = false) Boolean houseTrained,
            Pageable pageable) {
        
        Page<AnimalDto> animals = animalService.filterAnimals(
            species, breed, size, gender, minAge, maxAge, 
            goodWithKids, goodWithPets, houseTrained, pageable);
        return ResponseEntity.ok(animals);
    }
    
    @GetMapping("/shelter/{shelterId}")
    public ResponseEntity<Page<AnimalDto>> getAnimalsByShelterId(@PathVariable Long shelterId, 
                                                                 Pageable pageable) {
        Page<AnimalDto> animals = animalService.getAnimalsByShelterId(shelterId, pageable);
        return ResponseEntity.ok(animals);
    }
    
    @GetMapping("/shelter/{shelterId}/all")
    @PreAuthorize("hasRole('SHELTER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<List<AnimalDto>> getAllAnimalsByShelterId(@PathVariable Long shelterId) {
        List<AnimalDto> animals = animalService.getAnimalsByShelterId(shelterId);
        return ResponseEntity.ok(animals);
    }
    
    @GetMapping("/shelter/{shelterId}/count")
    public ResponseEntity<Long> getAvailableAnimalsCountByShelterId(@PathVariable Long shelterId) {
        long count = animalService.getAvailableAnimalsCountByShelterId(shelterId);
        return ResponseEntity.ok(count);
    }
}
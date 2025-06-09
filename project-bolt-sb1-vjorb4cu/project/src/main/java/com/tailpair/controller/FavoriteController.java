package com.tailpair.controller;

import com.tailpair.dto.AnimalDto;
import com.tailpair.security.UserPrincipal;
import com.tailpair.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @PostMapping("/animal/{animalId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long animalId,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        favoriteService.addToFavorites(userPrincipal.getId(), animalId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/animal/{animalId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long animalId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        favoriteService.removeFromFavorites(userPrincipal.getId(), animalId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/animal/{animalId}/check")
    public ResponseEntity<Boolean> isFavorited(@PathVariable Long animalId,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isFavorited = favoriteService.isFavorited(userPrincipal.getId(), animalId);
        return ResponseEntity.ok(isFavorited);
    }
    
    @GetMapping
    public ResponseEntity<Page<AnimalDto>> getFavoriteAnimals(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              Pageable pageable) {
        Page<AnimalDto> favoriteAnimals = favoriteService.getFavoriteAnimals(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(favoriteAnimals);
    }
    
    @GetMapping("/animal/{animalId}/count")
    public ResponseEntity<Long> getFavoriteCount(@PathVariable Long animalId) {
        long count = favoriteService.getFavoriteCount(animalId);
        return ResponseEntity.ok(count);
    }
}
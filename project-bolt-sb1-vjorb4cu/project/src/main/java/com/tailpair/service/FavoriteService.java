package com.tailpair.service;

import com.tailpair.dto.AnimalDto;
import com.tailpair.entity.Animal;
import com.tailpair.entity.Favorite;
import com.tailpair.entity.User;
import com.tailpair.exception.ResourceNotFoundException;
import com.tailpair.repository.AnimalRepository;
import com.tailpair.repository.FavoriteRepository;
import com.tailpair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AnimalRepository animalRepository;
    
    public void addToFavorites(Long userId, Long animalId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Animal animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + animalId));
        
        if (favoriteRepository.existsByUserIdAndAnimalId(userId, animalId)) {
            throw new IllegalArgumentException("Animal is already in favorites");
        }
        
        Favorite favorite = new Favorite(user, animal);
        favoriteRepository.save(favorite);
    }
    
    public void removeFromFavorites(Long userId, Long animalId) {
        if (!favoriteRepository.existsByUserIdAndAnimalId(userId, animalId)) {
            throw new ResourceNotFoundException("Favorite not found for user " + userId + " and animal " + animalId);
        }
        
        favoriteRepository.deleteByUserIdAndAnimalId(userId, animalId);
    }
    
    public boolean isFavorited(Long userId, Long animalId) {
        return favoriteRepository.existsByUserIdAndAnimalId(userId, animalId);
    }
    
    public Page<AnimalDto> getFavoriteAnimals(Long userId, Pageable pageable) {
        return favoriteRepository.findByUserId(userId, pageable)
            .map(favorite -> {
                AnimalDto animalDto = new AnimalDto(favorite.getAnimal());
                animalDto.setFavorited(true);
                return animalDto;
            });
    }
    
    public long getFavoriteCount(Long animalId) {
        return favoriteRepository.countByAnimalId(animalId);
    }
}
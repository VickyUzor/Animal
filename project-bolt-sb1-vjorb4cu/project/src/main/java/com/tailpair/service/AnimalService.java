package com.tailpair.service;

import com.tailpair.dto.AnimalDto;
import com.tailpair.entity.Animal;
import com.tailpair.entity.Shelter;
import com.tailpair.entity.User;
import com.tailpair.exception.ResourceNotFoundException;
import com.tailpair.repository.AnimalRepository;
import com.tailpair.repository.FavoriteRepository;
import com.tailpair.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnimalService {
    
    @Autowired
    private AnimalRepository animalRepository;
    
    @Autowired
    private ShelterRepository shelterRepository;
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    public AnimalDto createAnimal(AnimalDto animalDto, Long shelterId) {
        Shelter shelter = shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ResourceNotFoundException("Shelter not found with id: " + shelterId));
        
        Animal animal = new Animal();
        mapDtoToEntity(animalDto, animal);
        animal.setShelter(shelter);
        
        Animal savedAnimal = animalRepository.save(animal);
        return new AnimalDto(savedAnimal);
    }
    
    public AnimalDto getAnimalById(Long id) {
        Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + id));
        return new AnimalDto(animal);
    }
    
    public AnimalDto getAnimalById(Long id, Long userId) {
        Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + id));
        
        AnimalDto animalDto = new AnimalDto(animal);
        if (userId != null) {
            boolean isFavorited = favoriteRepository.existsByUserIdAndAnimalId(userId, id);
            animalDto.setFavorited(isFavorited);
        }
        
        return animalDto;
    }
    
    public AnimalDto updateAnimal(Long id, AnimalDto animalDto) {
        Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + id));
        
        mapDtoToEntity(animalDto, animal);
        Animal updatedAnimal = animalRepository.save(animal);
        return new AnimalDto(updatedAnimal);
    }
    
    public void deleteAnimal(Long id) {
        if (!animalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Animal not found with id: " + id);
        }
        animalRepository.deleteById(id);
    }
    
    public Page<AnimalDto> getAvailableAnimals(Pageable pageable) {
        return animalRepository.findAvailableAnimals(pageable)
            .map(AnimalDto::new);
    }
    
    public Page<AnimalDto> getAvailableAnimals(Pageable pageable, Long userId) {
        Page<AnimalDto> animals = animalRepository.findAvailableAnimals(pageable)
            .map(AnimalDto::new);
        
        if (userId != null) {
            animals.getContent().forEach(animalDto -> {
                boolean isFavorited = favoriteRepository.existsByUserIdAndAnimalId(userId, animalDto.getId());
                animalDto.setFavorited(isFavorited);
            });
        }
        
        return animals;
    }
    
    public Page<AnimalDto> searchAnimals(String searchTerm, Pageable pageable) {
        return animalRepository.searchAvailableAnimals(searchTerm, pageable)
            .map(AnimalDto::new);
    }
    
    public Page<AnimalDto> filterAnimals(
            Animal.Species species,
            String breed,
            Animal.Size size,
            Animal.Gender gender,
            Integer minAge,
            Integer maxAge,
            Boolean goodWithKids,
            Boolean goodWithPets,
            Boolean houseTrained,
            Pageable pageable) {
        
        return animalRepository.findAvailableAnimalsWithFilters(
            species, breed, size, gender, minAge, maxAge, 
            goodWithKids, goodWithPets, houseTrained, pageable
        ).map(AnimalDto::new);
    }
    
    public Page<AnimalDto> getAnimalsByShelterId(Long shelterId, Pageable pageable) {
        return animalRepository.findByShelterId(shelterId, pageable)
            .map(AnimalDto::new);
    }
    
    public List<AnimalDto> getAnimalsByShelterId(Long shelterId) {
        return animalRepository.findByShelterId(shelterId)
            .stream()
            .map(AnimalDto::new)
            .toList();
    }
    
    public long getAvailableAnimalsCountByShelterId(Long shelterId) {
        return animalRepository.countAvailableAnimalsByShelterId(shelterId);
    }
    
    private void mapDtoToEntity(AnimalDto dto, Animal entity) {
        entity.setName(dto.getName());
        entity.setSpecies(dto.getSpecies());
        entity.setBreed(dto.getBreed());
        entity.setAge(dto.getAge());
        entity.setGender(dto.getGender());
        entity.setSize(dto.getSize());
        entity.setWeight(dto.getWeight());
        entity.setColor(dto.getColor());
        entity.setDescription(dto.getDescription());
        entity.setMedicalHistory(dto.getMedicalHistory());
        entity.setVaccinated(dto.isVaccinated());
        entity.setSpayedNeutered(dto.isSpayedNeutered());
        entity.setHouseTrained(dto.isHouseTrained());
        entity.setGoodWithKids(dto.isGoodWithKids());
        entity.setGoodWithPets(dto.isGoodWithPets());
        entity.setStatus(dto.getStatus());
        entity.setAdoptionFee(dto.getAdoptionFee());
        entity.setImageUrls(dto.getImageUrls());
    }
}
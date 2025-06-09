package com.tailpair.repository;

import com.tailpair.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Page<Animal> findByStatus(Animal.Status status, Pageable pageable);
    Page<Animal> findBySpecies(Animal.Species species, Pageable pageable);
    Page<Animal> findByShelterId(Long shelterId, Pageable pageable);
    
    @Query("SELECT a FROM Animal a WHERE a.status = 'AVAILABLE'")
    Page<Animal> findAvailableAnimals(Pageable pageable);
    
    @Query("SELECT a FROM Animal a WHERE " +
           "a.status = 'AVAILABLE' AND " +
           "(:species IS NULL OR a.species = :species) AND " +
           "(:breed IS NULL OR LOWER(a.breed) LIKE LOWER(CONCAT('%', :breed, '%'))) AND " +
           "(:size IS NULL OR a.size = :size) AND " +
           "(:gender IS NULL OR a.gender = :gender) AND " +
           "(:minAge IS NULL OR a.age >= :minAge) AND " +
           "(:maxAge IS NULL OR a.age <= :maxAge) AND " +
           "(:goodWithKids IS NULL OR a.goodWithKids = :goodWithKids) AND " +
           "(:goodWithPets IS NULL OR a.goodWithPets = :goodWithPets) AND " +
           "(:houseTrained IS NULL OR a.houseTrained = :houseTrained)")
    Page<Animal> findAvailableAnimalsWithFilters(
        @Param("species") Animal.Species species,
        @Param("breed") String breed,
        @Param("size") Animal.Size size,
        @Param("gender") Animal.Gender gender,
        @Param("minAge") Integer minAge,
        @Param("maxAge") Integer maxAge,
        @Param("goodWithKids") Boolean goodWithKids,
        @Param("goodWithPets") Boolean goodWithPets,
        @Param("houseTrained") Boolean houseTrained,
        Pageable pageable
    );
    
    @Query("SELECT a FROM Animal a WHERE " +
           "a.status = 'AVAILABLE' AND " +
           "(LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.breed) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Animal> searchAvailableAnimals(@Param("search") String search, Pageable pageable);
    
    List<Animal> findByShelterId(Long shelterId);
    
    @Query("SELECT COUNT(a) FROM Animal a WHERE a.shelter.id = :shelterId AND a.status = 'AVAILABLE'")
    long countAvailableAnimalsByShelterId(@Param("shelterId") Long shelterId);
}
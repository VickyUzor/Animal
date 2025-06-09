package com.tailpair.repository;

import com.tailpair.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByUserId(Long userId, Pageable pageable);
    Optional<Favorite> findByUserIdAndAnimalId(Long userId, Long animalId);
    boolean existsByUserIdAndAnimalId(Long userId, Long animalId);
    
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.animal.id = :animalId")
    long countByAnimalId(@Param("animalId") Long animalId);
    
    void deleteByUserIdAndAnimalId(Long userId, Long animalId);
}
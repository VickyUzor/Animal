package com.tailpair.repository;

import com.tailpair.entity.Adoption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    Page<Adoption> findByAdopterId(Long adopterId, Pageable pageable);
    Page<Adoption> findByAnimalId(Long animalId, Pageable pageable);
    Page<Adoption> findByStatus(Adoption.Status status, Pageable pageable);
    
    Optional<Adoption> findByAdopterIdAndAnimalId(Long adopterId, Long animalId);
    boolean existsByAdopterIdAndAnimalId(Long adopterId, Long animalId);
    
    @Query("SELECT a FROM Adoption a WHERE " +
           "a.animal.shelter.id = :shelterId")
    Page<Adoption> findByShelterId(@Param("shelterId") Long shelterId, Pageable pageable);
    
    @Query("SELECT a FROM Adoption a WHERE " +
           "a.animal.shelter.id = :shelterId AND a.status = :status")
    Page<Adoption> findByShelterIdAndStatus(@Param("shelterId") Long shelterId, @Param("status") Adoption.Status status, Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM Adoption a WHERE " +
           "a.animal.shelter.id = :shelterId AND a.status = 'PENDING'")
    long countPendingAdoptionsByShelterId(@Param("shelterId") Long shelterId);
}
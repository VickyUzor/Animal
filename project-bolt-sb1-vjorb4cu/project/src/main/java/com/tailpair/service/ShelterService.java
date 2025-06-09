package com.tailpair.service;

import com.tailpair.dto.ShelterDto;
import com.tailpair.entity.Shelter;
import com.tailpair.entity.User;
import com.tailpair.exception.ResourceNotFoundException;
import com.tailpair.repository.ShelterRepository;
import com.tailpair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShelterService {
    
    @Autowired
    private ShelterRepository shelterRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public ShelterDto createShelter(ShelterDto shelterDto, Long adminId) {
        User admin = userRepository.findById(adminId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + adminId));
        
        Shelter shelter = new Shelter();
        mapDtoToEntity(shelterDto, shelter);
        shelter.setAdmin(admin);
        
        Shelter savedShelter = shelterRepository.save(shelter);
        return new ShelterDto(savedShelter);
    }
    
    public ShelterDto getShelterById(Long id) {
        Shelter shelter = shelterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Shelter not found with id: " + id));
        return new ShelterDto(shelter);
    }
    
    public ShelterDto getShelterByAdminId(Long adminId) {
        Shelter shelter = shelterRepository.findByAdminId(adminId)
            .orElseThrow(() -> new ResourceNotFoundException("Shelter not found for admin id: " + adminId));
        return new ShelterDto(shelter);
    }
    
    public ShelterDto updateShelter(Long id, ShelterDto shelterDto) {
        Shelter shelter = shelterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Shelter not found with id: " + id));
        
        mapDtoToEntity(shelterDto, shelter);
        Shelter updatedShelter = shelterRepository.save(shelter);
        return new ShelterDto(updatedShelter);
    }
    
    public void deleteShelter(Long id) {
        if (!shelterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shelter not found with id: " + id);
        }
        shelterRepository.deleteById(id);
    }
    
    public Page<ShelterDto> getAllShelters(Pageable pageable) {
        return shelterRepository.findAll(pageable)
            .map(ShelterDto::new);
    }
    
    public Page<ShelterDto> getVerifiedShelters(Pageable pageable) {
        return shelterRepository.findByVerified(true, pageable)
            .map(ShelterDto::new);
    }
    
    public Page<ShelterDto> searchShelters(String searchTerm, Pageable pageable) {
        return shelterRepository.findBySearchTerm(searchTerm, pageable)
            .map(ShelterDto::new);
    }
    
    public List<ShelterDto> getSheltersByLocation(String city, String state) {
        return shelterRepository.findByCityAndState(city, state)
            .stream()
            .map(ShelterDto::new)
            .toList();
    }
    
    public List<ShelterDto> getSheltersByZipCode(String zipCode) {
        return shelterRepository.findByZipCode(zipCode)
            .stream()
            .map(ShelterDto::new)
            .toList();
    }
    
    public ShelterDto verifyShelter(Long id) {
        Shelter shelter = shelterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Shelter not found with id: " + id));
        
        shelter.setVerified(true);
        Shelter updatedShelter = shelterRepository.save(shelter);
        return new ShelterDto(updatedShelter);
    }
    
    private void mapDtoToEntity(ShelterDto dto, Shelter entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setZipCode(dto.getZipCode());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setWebsite(dto.getWebsite());
        entity.setVerified(dto.isVerified());
    }
}
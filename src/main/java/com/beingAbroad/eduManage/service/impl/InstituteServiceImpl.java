package com.beingAbroad.eduManage.service.impl;

import com.beingAbroad.eduManage.dto.InstituteRequestDTO;
import com.beingAbroad.eduManage.dto.InstituteResponseDTO;
import com.beingAbroad.eduManage.entity.Institute;
import com.beingAbroad.eduManage.exception.EmailAlreadyExistsException;
import com.beingAbroad.eduManage.exception.InstituteCodeAlreadyExistsException;
import com.beingAbroad.eduManage.exception.ResourceNotFoundException;
import com.beingAbroad.eduManage.repository.InstituteRepository;
import com.beingAbroad.eduManage.service.InstituteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class InstituteServiceImpl implements InstituteService {
    private static final Logger logger = LoggerFactory.getLogger(InstituteServiceImpl.class);

    private final InstituteRepository instituteRepository;

    @Autowired
    public InstituteServiceImpl(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
    }


    public List<InstituteResponseDTO> getAllInstitutes() {
        logger.info("Service: Retrieving all institutes");
        List<Institute> institutes = instituteRepository.findAll();
        List<InstituteResponseDTO> responseDTOs = institutes.stream()
                .map(institute -> InstituteResponseDTO.builder()
                        .id(institute.getId())
                        .name(institute.getName())
                        .location(institute.getLocation())
                        .email(institute.getEmail())
                        .phoneNumber(institute.getPhoneNumber())
                        .instituteCode(institute.getInstituteCode())
                        .build())
                .toList();
        logger.info("Service: Successfully retrieved {} institutes", responseDTOs.size());
        return responseDTOs;
    }


    public InstituteResponseDTO getInstitute(UUID id) {
        logger.info("Service: Retrieving institute with id: {}", id);

        Institute institute = instituteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Service: Institution not found with id: {}", id);
                    return new ResourceNotFoundException("Institution not found with id: " + id);
                });
        InstituteResponseDTO responseDTO = InstituteResponseDTO.builder()
                .id(institute.getId())
                .name(institute.getName())
                .location(institute.getLocation())
                .email(institute.getEmail())
                .phoneNumber(institute.getPhoneNumber())
                .instituteCode(institute.getInstituteCode())
                .build();
        logger.info("Service: Successfully retrieved institute with id: {}", id);
        return responseDTO;
    }


    public InstituteResponseDTO registerInstitute(InstituteRequestDTO instituteRequestDTO) {
        logger.info("Service: Creating new institute with name: {}", instituteRequestDTO.getName());

        if (instituteRepository.existsByInstituteCode(instituteRequestDTO.getInstituteCode())) {
            logger.error("Service: Institute code already exists: {}", instituteRequestDTO.getInstituteCode());
            throw new InstituteCodeAlreadyExistsException("Institute with the same institute code already exists");
        }

        if (instituteRepository.existsByEmail(instituteRequestDTO.getEmail())) {
            logger.error("Service: Email already exists: {}", instituteRequestDTO.getEmail());
            throw new EmailAlreadyExistsException("Institute with the same email already exists");
        }

        Institute institute = Institute.builder()
                .name(instituteRequestDTO.getName())
                .location(instituteRequestDTO.getLocation())
                .email(instituteRequestDTO.getEmail())
                .phoneNumber(instituteRequestDTO.getPhoneNumber())
                .instituteCode(instituteRequestDTO.getInstituteCode())
                .build();

        Institute savedInstitute = instituteRepository.save(institute);
        InstituteResponseDTO responseDTO = InstituteResponseDTO.builder()
                .id(savedInstitute.getId())
                .name(savedInstitute.getName())
                .location(savedInstitute.getLocation())
                .email(savedInstitute.getEmail())
                .phoneNumber(savedInstitute.getPhoneNumber())
                .instituteCode(savedInstitute.getInstituteCode())
                .build();

        logger.info("Service: Successfully created institute with id: {}", savedInstitute.getId());
        return responseDTO;
    }


    public InstituteResponseDTO updateInstitute(UUID id, InstituteRequestDTO instituteRequestDTO) {
        logger.info("Service: Updating institute with id: {}", id);
        Institute existingInstitute = instituteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Service: Institute not found with id: {}", id);
                    return new ResourceNotFoundException("Institute not found with id: " + id);
                });

        boolean emailChanged = !existingInstitute.getEmail().equals(instituteRequestDTO.getEmail());
        boolean instituteCodeChanged = !existingInstitute.getInstituteCode().equals(instituteRequestDTO.getInstituteCode());

        if (emailChanged && instituteRepository.existsByEmail(instituteRequestDTO.getEmail())) {
            logger.error("Service: Update failed - Another institute with the same email already exists: {}", instituteRequestDTO.getEmail());
            throw new EmailAlreadyExistsException("Another institute with the same email already exists.");
        }

        if (instituteCodeChanged && instituteRepository.existsByInstituteCode(instituteRequestDTO.getInstituteCode())) {
            logger.error("Service: Update failed - Another institute with the same institute code already exists: {}", instituteRequestDTO.getInstituteCode());
            throw new InstituteCodeAlreadyExistsException("Another institute with the same institute code already exists.");
        }

        existingInstitute.setName(instituteRequestDTO.getName());
        existingInstitute.setLocation(instituteRequestDTO.getLocation());
        existingInstitute.setEmail(instituteRequestDTO.getEmail());
        existingInstitute.setPhoneNumber(instituteRequestDTO.getPhoneNumber());
        existingInstitute.setInstituteCode(instituteRequestDTO.getInstituteCode());

        logger.info("Service: Saving updated institute to database with id: {}", existingInstitute.getId());
        Institute updatedInstitute = instituteRepository.save(existingInstitute);

        InstituteResponseDTO responseDTO = InstituteResponseDTO.builder()
                .id(updatedInstitute.getId())
                .name(updatedInstitute.getName())
                .location(updatedInstitute.getLocation())
                .email(updatedInstitute.getEmail())
                .phoneNumber(updatedInstitute.getPhoneNumber())
                .instituteCode(updatedInstitute.getInstituteCode())
                .build();

        logger.info("Service: Successfully updated institute with id: {}", updatedInstitute.getId());
        return responseDTO;
    }


    public void deleteInstitute(UUID id) {
        logger.info("Service: Deleting institute with id: {}", id);
        Institute institute = instituteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Service: Institute not found with id: {}", id);
                    return new ResourceNotFoundException("Institute not found with id: " + id);
                });
        instituteRepository.delete(institute);
        logger.info("Service: Successfully deleted institute with id: {}", id);
    }
}

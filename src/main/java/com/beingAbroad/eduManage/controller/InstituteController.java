package com.beingAbroad.eduManage.controller;

import com.beingAbroad.eduManage.dto.InstituteRequestDTO;
import com.beingAbroad.eduManage.dto.InstituteResponseDTO;
import com.beingAbroad.eduManage.service.InstituteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/institute")
@Validated
public class InstituteController {
    private static final Logger logger = LoggerFactory.getLogger(InstituteController.class);
    private final InstituteService instituteService;

    @Autowired
    public  InstituteController (InstituteService instituteService) {
    this.instituteService=instituteService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<InstituteResponseDTO> addInstitute(@Valid @RequestBody InstituteRequestDTO instituteRequestDTO) {
        logger.info("Controller: Received request to create institute with name: {}", instituteRequestDTO.getName());
        InstituteResponseDTO savedInstitute = instituteService.registerInstitute(instituteRequestDTO);
        logger.info("Controller: Successfully registered institute: {}", savedInstitute.getName());
        return new ResponseEntity<>(savedInstitute, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<InstituteResponseDTO> updateInstitute(@PathVariable UUID id, @Valid @RequestBody InstituteRequestDTO instituteRequestDTO) {
        logger.info("Controller: Received request to update institute with id: {}", id);
        InstituteResponseDTO updatedInstitute = instituteService.updateInstitute(id, instituteRequestDTO);
        logger.info("Controller: Successfully updated institute with id: {}", updatedInstitute.getId());
        return new ResponseEntity<>(updatedInstitute, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituteResponseDTO> getInstitute(@PathVariable UUID id) {
        logger.info("Controller: Received request to retrieve institute with id: {}", id);
        InstituteResponseDTO institute = instituteService.getInstitute(id);
        logger.info("Controller: Successfully retrieved institute with id: {}", id);
        return new ResponseEntity<>(institute, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<InstituteResponseDTO>> getAllInstitutes() {
        logger.info("Controller: Received request to retrieve all institutes");
        List<InstituteResponseDTO> institutes = instituteService.getAllInstitutes();
        logger.info("Controller: Successfully retrieved {} institutes", institutes.size());
        return new ResponseEntity<>(institutes, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInstitute(@PathVariable UUID id) {
        logger.info("Controller: Received request to delete institute with id: {}", id);
        instituteService.deleteInstitute(id);
        logger.info("Controller: Successfully deleted institute with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

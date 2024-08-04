package com.beingAbroad.eduManage.service;

import com.beingAbroad.eduManage.dto.InstituteRequestDTO;
import com.beingAbroad.eduManage.dto.InstituteResponseDTO;

import java.util.List;
import java.util.UUID;

public interface InstituteService {
    InstituteResponseDTO registerInstitute(InstituteRequestDTO instituteRequestDTO);
    InstituteResponseDTO updateInstitute(UUID id, InstituteRequestDTO instituteRequestDTO);
    InstituteResponseDTO getInstitute(UUID id);
    List<InstituteResponseDTO> getAllInstitutes();
    void deleteInstitute(UUID id);
}

package com.beingAbroad.eduManage.service.impl;

import com.beingAbroad.eduManage.dto.InstituteRequestDTO;
import com.beingAbroad.eduManage.dto.InstituteResponseDTO;
import com.beingAbroad.eduManage.entity.Institute;
import com.beingAbroad.eduManage.exception.EmailAlreadyExistsException;
import com.beingAbroad.eduManage.exception.InstituteCodeAlreadyExistsException;
import com.beingAbroad.eduManage.exception.ResourceNotFoundException;
import com.beingAbroad.eduManage.repository.InstituteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstituteServiceImplTest {

    @Mock
    private InstituteRepository instituteRepository;

    @InjectMocks
    private InstituteServiceImpl instituteService;

    @Captor
    private ArgumentCaptor<Institute> instituteArgumentCaptor;

    private InstituteRequestDTO requestDTO;
    private Institute savedInstitute;

    @BeforeEach
    void setUp() {
        requestDTO = new InstituteRequestDTO();
        requestDTO.setName("Test Institute");
        requestDTO.setLocation("Test Location");
        requestDTO.setEmail("test@example.com");
        requestDTO.setPhoneNumber("1234567890");
        requestDTO.setInstituteCode("NEW_CODE");

        savedInstitute = Institute.builder()
                .id(UUID.randomUUID())
                .name(requestDTO.getName())
                .location(requestDTO.getLocation())
                .email(requestDTO.getEmail())
                .phoneNumber(requestDTO.getPhoneNumber())
                .instituteCode(requestDTO.getInstituteCode())
                .build();
    }

    @Test
    void shouldGetAllInstitutes() {
        List<Institute> institutes = List.of(savedInstitute);
        when(instituteRepository.findAll()).thenReturn(institutes);

        List<InstituteResponseDTO> responseDTOs = instituteService.getAllInstitutes();

        assertThat(responseDTOs).isNotNull();
        assertThat(responseDTOs.get(0).getName()).isEqualTo(savedInstitute.getName());
        assertThat(responseDTOs.get(0).getEmail()).isEqualTo(savedInstitute.getEmail());
        verify(instituteRepository).findAll();
    }


    @Test
    void shouldThrowResourceNotFoundExceptionWhenInstituteNotFound() {
        UUID id = UUID.randomUUID();
        when(instituteRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instituteService.getInstitute(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Institution not found with id: " + id);

        verify(instituteRepository).findById(id);
    }

    @Test
    void shouldGetInstituteById() {
        UUID id = savedInstitute.getId();
        when(instituteRepository.findById(id)).thenReturn(Optional.of(savedInstitute));

        InstituteResponseDTO responseDTO = instituteService.getInstitute(id);

        assertThat(responseDTO.getId()).isEqualTo(id);
        assertThat(responseDTO.getName()).isEqualTo(savedInstitute.getName());
        assertThat(responseDTO.getEmail()).isEqualTo(savedInstitute.getEmail());
        verify(instituteRepository).findById(id);
    }

    @Test
    void shouldThrowInstituteCodeAlreadyExistsExceptionWhenInstituteCodeExists() {

        when(instituteRepository.existsByInstituteCode(anyString())).thenReturn(true);
        assertThatThrownBy(() -> instituteService.registerInstitute(requestDTO))
                .isInstanceOf(InstituteCodeAlreadyExistsException.class)
                .hasMessage("Institute with the same institute code already exists");

        verify(instituteRepository, never()).save(any(Institute.class));
    }

    @Test
    void shouldThrowEmailAlreadyExistsExceptionWhenEmailExists() {
        when(instituteRepository.existsByInstituteCode(anyString())).thenReturn(false);
        when(instituteRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> instituteService.registerInstitute(requestDTO))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Institute with the same email already exists");

        verify(instituteRepository, never()).save(any(Institute.class));
    }

    @Test
    void shouldRegisterInstituteSuccessfully() {
        when(instituteRepository.existsByInstituteCode(anyString())).thenReturn(false);
        when(instituteRepository.existsByEmail(anyString())).thenReturn(false);
        when(instituteRepository.save(any(Institute.class))).thenReturn(savedInstitute);

        InstituteResponseDTO responseDTO = instituteService.registerInstitute(requestDTO);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(savedInstitute.getId());
        assertThat(responseDTO.getName()).isEqualTo(savedInstitute.getName());
        assertThat(responseDTO.getEmail()).isEqualTo(savedInstitute.getEmail());

        verify(instituteRepository).save(instituteArgumentCaptor.capture());
        Institute capturedInstitute = instituteArgumentCaptor.getValue();
        assertThat(capturedInstitute.getName()).isEqualTo(requestDTO.getName());
        assertThat(capturedInstitute.getEmail()).isEqualTo(requestDTO.getEmail());
        assertThat(capturedInstitute.getLocation()).isEqualTo(requestDTO.getLocation());
        assertThat(capturedInstitute.getPhoneNumber()).isEqualTo(requestDTO.getPhoneNumber());
        assertThat(capturedInstitute.getInstituteCode()).isEqualTo(requestDTO.getInstituteCode());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsWhileUpdating() {

        UUID id = savedInstitute.getId();
        InstituteRequestDTO updatedRequestDTO = new InstituteRequestDTO();
        updatedRequestDTO.setEmail("new@example.com");
        when(instituteRepository.findById(id)).thenReturn(Optional.of(savedInstitute));
        when(instituteRepository.existsByEmail(anyString())).thenReturn(true);


        assertThatThrownBy(() -> instituteService.updateInstitute(id, updatedRequestDTO))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Another institute with the same email already exists.");

        verify(instituteRepository, never()).save(any(Institute.class));
    }

    @Test
    void shouldThrowExceptionWhenInstituteCodeAlreadyExistsWhileUpdating() {

        UUID id = savedInstitute.getId();
        InstituteRequestDTO updatedRequestDTO = new InstituteRequestDTO();
        updatedRequestDTO.setInstituteCode("EXISTING_CODE");
        when(instituteRepository.findById(id)).thenReturn(Optional.of(savedInstitute));
        when(instituteRepository.existsByInstituteCode(anyString())).thenReturn(true);


        assertThatThrownBy(() -> instituteService.updateInstitute(id, updatedRequestDTO))
                .isInstanceOf(InstituteCodeAlreadyExistsException.class)
                .hasMessage("Another institute with the same institute code already exists.");

        verify(instituteRepository, never()).save(any(Institute.class));
    }

    @Test
    void shouldUpdateInstituteSuccessfully() {
        // given
        UUID id = savedInstitute.getId();
        InstituteRequestDTO updatedRequestDTO = new InstituteRequestDTO();
        updatedRequestDTO.setName("Updated Institute");
        updatedRequestDTO.setLocation("Updated Location");
        updatedRequestDTO.setEmail("updated@example.com");
        updatedRequestDTO.setPhoneNumber("0987654321");
        updatedRequestDTO.setInstituteCode("UPDATED_CODE");

        when(instituteRepository.findById(id)).thenReturn(Optional.of(savedInstitute));
        when(instituteRepository.existsByEmail(anyString())).thenReturn(false);
        when(instituteRepository.existsByInstituteCode(anyString())).thenReturn(false);
        when(instituteRepository.save(any(Institute.class))).thenReturn(savedInstitute);


        InstituteResponseDTO responseDTO = instituteService.updateInstitute(id, updatedRequestDTO);


        assertThat(responseDTO.getId()).isEqualTo(id);
        assertThat(responseDTO.getName()).isEqualTo(updatedRequestDTO.getName());
        assertThat(responseDTO.getEmail()).isEqualTo(updatedRequestDTO.getEmail());
        assertThat(responseDTO.getLocation()).isEqualTo(updatedRequestDTO.getLocation());

        verify(instituteRepository).save(instituteArgumentCaptor.capture());
        Institute capturedInstitute = instituteArgumentCaptor.getValue();
        assertThat(capturedInstitute.getName()).isEqualTo(updatedRequestDTO.getName());
        assertThat(capturedInstitute.getEmail()).isEqualTo(updatedRequestDTO.getEmail());
        assertThat(capturedInstitute.getLocation()).isEqualTo(updatedRequestDTO.getLocation());
        assertThat(capturedInstitute.getPhoneNumber()).isEqualTo(updatedRequestDTO.getPhoneNumber());
        assertThat(capturedInstitute.getInstituteCode()).isEqualTo(updatedRequestDTO.getInstituteCode());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteInstituteNotFound() {
        UUID id = UUID.randomUUID();
        when(instituteRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> instituteService.deleteInstitute(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Institute not found with id: " + id);

        verify(instituteRepository).findById(id);
        verify(instituteRepository, never()).delete(any(Institute.class));
    }

    @Test
    void shouldDeleteInstituteSuccessfully() {
        UUID id = savedInstitute.getId();
        when(instituteRepository.findById(id)).thenReturn(Optional.of(savedInstitute));
        instituteService.deleteInstitute(id);
        verify(instituteRepository).findById(id);
        verify(instituteRepository).delete(savedInstitute);
    }
}
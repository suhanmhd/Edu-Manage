package com.beingAbroad.eduManage.controller;

import com.beingAbroad.eduManage.auth.services.JwtService;
import com.beingAbroad.eduManage.dto.InstituteRequestDTO;
import com.beingAbroad.eduManage.dto.InstituteResponseDTO;
import com.beingAbroad.eduManage.service.InstituteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)


@WebMvcTest(InstituteController.class)
@WithMockUser(authorities = "ADMIN")
class InstituteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private InstituteService instituteService;

    private InstituteResponseDTO instituteResponseDTOOne;
    private InstituteResponseDTO instituteResponseDTOTwo;
    private InstituteRequestDTO instituteRequestDTO;
    private List<InstituteResponseDTO> instituteList;

    @BeforeEach
    void setUp() {
        instituteResponseDTOOne = new InstituteResponseDTO(UUID.randomUUID(), "Institute One", "Location One", "email1@example.com", "1234567890", "Code1");
        instituteResponseDTOTwo = new InstituteResponseDTO(UUID.randomUUID(), "Institute Two", "Location Two", "email2@example.com", "0987654321", "Code2");

        instituteRequestDTO = new InstituteRequestDTO("Institute Three", "Location Three", "email3@example.com", "1122334455", "Code3");

        instituteList = new ArrayList<>();
        instituteList.add(instituteResponseDTOOne);
        instituteList.add(instituteResponseDTOTwo);
    }

    @AfterEach
    void tearDown() {
        instituteList.clear();
    }

    @Test

    void addInstitute() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(instituteRequestDTO);

        when(instituteService.registerInstitute(any(InstituteRequestDTO.class))).thenReturn(instituteResponseDTOOne);

        this.mockMvc.perform(post("/api/v1/institute/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void updateInstitute() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(instituteRequestDTO);

        when(instituteService.updateInstitute(any(UUID.class), any(InstituteRequestDTO.class))).thenReturn(instituteResponseDTOOne);

        this.mockMvc.perform(put("/api/v1/institute/{id}", instituteResponseDTOOne.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getInstitute() throws Exception {
        when(instituteService.getInstitute(any(UUID.class))).thenReturn(instituteResponseDTOOne);

        this.mockMvc.perform(get("/api/v1/institute/{id}", instituteResponseDTOOne.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllInstitutes() throws Exception {
        when(instituteService.getAllInstitutes()).thenReturn(instituteList);

        this.mockMvc.perform(get("/api/v1/institute/getAll"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteInstitute() throws Exception {
        doNothing().when(instituteService).deleteInstitute(any(UUID.class));

        this.mockMvc.perform(delete("/api/v1/institute/delete/{id}", instituteResponseDTOOne.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))

                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

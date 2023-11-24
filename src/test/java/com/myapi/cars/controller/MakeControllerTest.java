package com.myapi.cars.controller;

import com.myapi.cars.config.WebTestConfig;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.service.MakeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MakeController.class})
@WithMockUser
@Import(WebTestConfig.class)
@ActiveProfiles(value = "test")
public class MakeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MakeService makeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void create_whenMakeDTOIsValid_success() throws Exception {
        when(makeService.create(any(MakeDTO.class))).thenReturn(1L);

        MakeDTO makeDTO = MakeDTO.builder().name("name").build();

        mockMvc.perform(post("/api/v1/make").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeDTO)))
                .andExpect(status().isCreated()).andExpect(content().string("1"));

        verify(makeService).create(makeDTO);
        verifyNoMoreInteractions(makeService);
    }

    @Test
    public void create_whenMakeDTOIsNull_statusIsBadRequest() throws Exception {
        MakeDTO makeDTO = null;

        mockMvc.perform(post("/api/v1/make").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(makeService);
    }

    @Test
    public void delete_success() throws Exception {
        mockMvc.perform(delete("/api/v1/make/1"))
                .andExpect(status().isNoContent());

        verify(makeService).deleteById(1L);
        verifyNoMoreInteractions(makeService);
    }

    @Test
    public void delete_whenMakeIdIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/make/invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(makeService);
    }

    @Test
    public void update_whenMakeDTOIsValid_success() throws Exception {
        MakeDTO makeDTOToDisplay = MakeDTO.builder().name("nameDisplay").build();
        MakeDTO makeDTO = MakeDTO.builder().name("name").build();

        when(makeService.update(any(MakeDTO.class), any(Long.class))).thenReturn(makeDTOToDisplay);

        mockMvc.perform(patch("/api/v1/make/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeDTO)))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(makeDTOToDisplay)));

        verify(makeService).update(makeDTO, 1L);
        verifyNoMoreInteractions(makeService);
    }

    @Test
    public void update_whenMakeDTOIsNull_statusIsBadRequest() throws Exception {
        MakeDTO makeDTO = null;

        mockMvc.perform(patch("/api/v1/make/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(makeService);
    }

    @Test
    public void update_whenMakeIdIsInvalid_statusIsBadRequest() throws Exception {
        MakeDTO makeDTO = MakeDTO.builder().name("name").build();

        mockMvc.perform(patch("/api/v1/make/invalid").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(makeService);
    }

    @Test
    public void getById_success() throws Exception {
        MakeDTO makeDTO = MakeDTO.builder().name("name").build();

        when(makeService.findById(any(Long.class))).thenReturn(makeDTO);

        mockMvc.perform(get("/api/v1/make/1"))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(makeDTO)));

        verify(makeService).findById(1L);
        verifyNoMoreInteractions(makeService);
    }

    @Test
    public void getById_whenMakeIdIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/make/invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(makeService);
    }

    @Test
    public void getAll_success() throws Exception {
        List<MakeDTO> makeDTOList = List.of(MakeDTO.builder().name("name").build());
        DTOSearchResponse dtoSearchResponse = DTOSearchResponse.builder().data(makeDTOList).build();

        when(makeService.findAll(any())).thenReturn(dtoSearchResponse);

        mockMvc.perform(get("/api/v1/make"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoSearchResponse)));

        verify(makeService).findAll(any());
        verifyNoMoreInteractions(makeService);
    }

    // It is not depends on the parameter selection.
    @Test
    public void getAll_whenLimitIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/make?limit=invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(makeService);
    }

}

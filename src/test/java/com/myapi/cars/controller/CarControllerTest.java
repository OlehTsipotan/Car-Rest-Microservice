package com.myapi.cars.controller;

import com.myapi.cars.config.WebTestConfig;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.service.CarService;
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

@WebMvcTest({CarController.class})
@WithMockUser
@Import(WebTestConfig.class)
@ActiveProfiles(value = "test")
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void create_whenCarDTOIsValid_success() throws Exception {
        when(carService.create(any(CarDTO.class))).thenReturn(1L);

        CarDTO carDTO = CarDTO.builder().model("model").build();

        mockMvc.perform(post("/api/v1/car").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isCreated()).andExpect(content().string("1"));

        verify(carService).create(carDTO);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void create_whenCarDTOIsNull_statusIsBadRequest() throws Exception {
        CarDTO carDTO = null;

        mockMvc.perform(post("/api/v1/car").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(carService);
    }

    @Test
    public void delete_success() throws Exception {
        mockMvc.perform(delete("/api/v1/car/1"))
                .andExpect(status().isNoContent());

        verify(carService).deleteById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void delete_whenCarIdIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/car/invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(carService);
    }

    @Test
    public void update_whenCarDTOIsValid_success() throws Exception {
        CarDTO carDTOToDisplay = CarDTO.builder().model("modelDisplay").build();
        CarDTO carDTO = CarDTO.builder().model("model").build();

        when(carService.update(any(CarDTO.class), any(Long.class))).thenReturn(carDTOToDisplay);

        mockMvc.perform(patch("/api/v1/car/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(carDTOToDisplay)));

        verify(carService).update(carDTO, 1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void update_whenCarDTOIsNull_statusIsBadRequest() throws Exception {
        CarDTO carDTO = null;

        mockMvc.perform(patch("/api/v1/car/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(carService);
    }

    @Test
    public void update_whenCarIdIsInvalid_statusIsBadRequest() throws Exception {
        CarDTO carDTO = CarDTO.builder().model("model").build();

        mockMvc.perform(patch("/api/v1/car/invalid").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(carService);
    }

    @Test
    public void getById_success() throws Exception {
        CarDTO carDTO = CarDTO.builder().model("model").build();

        when(carService.findById(any(Long.class))).thenReturn(carDTO);

        mockMvc.perform(get("/api/v1/car/1"))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(carDTO)));

        verify(carService).findById(1L);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void getById_whenCarIdIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/car/invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(carService);
    }

    @Test
    public void getAll_success() throws Exception {
        List<CarDTO> carDTOList = List.of(CarDTO.builder().model("model").build());
        DTOSearchResponse dtoSearchResponse = DTOSearchResponse.builder().data(carDTOList).build();

        when(carService.findAll(any(), any(), any(), any(), any())).thenReturn(dtoSearchResponse);

        mockMvc.perform(get("/api/v1/car"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoSearchResponse)));

        verify(carService).findAll(any(), any(), any(), any(), any());
        verifyNoMoreInteractions(carService);
    }

    // It is not depends on the parameter selection.
    @Test
    public void getAll_whenLimitIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/car?limit=invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(carService);
    }

}

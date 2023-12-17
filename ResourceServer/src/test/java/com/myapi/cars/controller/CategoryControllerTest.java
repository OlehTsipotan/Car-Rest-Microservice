package com.myapi.cars.controller;

import com.myapi.cars.config.WebTestConfig;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.service.CategoryService;
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

@WebMvcTest({CategoryController.class})
@WithMockUser
@Import(WebTestConfig.class)
@ActiveProfiles(value = "test")
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void create_whenCategoryDTOIsValid_success() throws Exception {
        when(categoryService.create(any(CategoryDTO.class))).thenReturn(1L);

        CategoryDTO categoryDTO = CategoryDTO.builder().name("name").build();

        mockMvc.perform(post("/api/v1/category").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated()).andExpect(content().string("1"));

        verify(categoryService).create(categoryDTO);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void create_whenCategoryDTOIsNull_statusIsBadRequest() throws Exception {
        CategoryDTO categoryDTO = null;

        mockMvc.perform(post("/api/v1/category").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }

    @Test
    public void delete_success() throws Exception {
        mockMvc.perform(delete("/api/v1/category/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteById(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void delete_whenCategoryIdIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/category/invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }

    @Test
    public void update_whenCategoryDTOIsValid_success() throws Exception {
        CategoryDTO categoryDTOToDisplay = CategoryDTO.builder().name("nameDisplay").build();
        CategoryDTO categoryDTO = CategoryDTO.builder().name("name").build();

        when(categoryService.update(any(CategoryDTO.class), any(Long.class))).thenReturn(categoryDTOToDisplay);

        mockMvc.perform(patch("/api/v1/category/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(categoryDTOToDisplay)));

        verify(categoryService).update(categoryDTO, 1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void update_whenCategoryDTOIsNull_statusIsBadRequest() throws Exception {
        CategoryDTO categoryDTO = null;

        mockMvc.perform(patch("/api/v1/category/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }

    @Test
    public void update_whenCategoryIdIsInvalid_statusIsBadRequest() throws Exception {
        CategoryDTO categoryDTO = CategoryDTO.builder().name("name").build();

        mockMvc.perform(patch("/api/v1/category/invalid").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }

    @Test
    public void getById_success() throws Exception {
        CategoryDTO categoryDTO = CategoryDTO.builder().name("name").build();

        when(categoryService.findById(any(Long.class))).thenReturn(categoryDTO);

        mockMvc.perform(get("/api/v1/category/1"))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(categoryDTO)));

        verify(categoryService).findById(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void getById_whenCategoryIdIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/category/invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }

    @Test
    public void getAll_success() throws Exception {
        List<CategoryDTO> categoryDTOList = List.of(CategoryDTO.builder().name("name").build());
        DTOSearchResponse dtoSearchResponse = DTOSearchResponse.builder().data(categoryDTOList).build();

        when(categoryService.findAll(any())).thenReturn(dtoSearchResponse);

        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoSearchResponse)));

        verify(categoryService).findAll(any());
        verifyNoMoreInteractions(categoryService);
    }

    // It is not depends on the parameter selection.
    @Test
    public void getAll_whenLimitIsInvalid_statusIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/category?limit=invalid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }

}
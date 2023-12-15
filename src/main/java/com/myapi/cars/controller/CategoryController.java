package com.myapi.cars.controller;

import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Category;
import com.myapi.cars.service.CategoryService;
import com.myapi.cars.service.CategoryService;
import com.myapi.cars.utility.PaginationSortingUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create the Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Category already exists", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }

    @Operation(summary = "Delete the Category by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)})
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
    }

    @Operation(summary = "Update the Category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)})
    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO update(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
        return categoryService.update(categoryDTO, categoryId);
    }

    @Operation(summary = "Retrieve the Category by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)})

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getById(@PathVariable Long categoryId) {
        return categoryService.findById(categoryId);
    }

    @Operation(summary = "Retrieve the Categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categories retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))})})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DTOSearchResponse getAll(@RequestParam(defaultValue = "100") int limit,
                                    @RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PaginationSortingUtils.getPageable(limit, offset, sort);
        return categoryService.findAll(pageable);
    }

}

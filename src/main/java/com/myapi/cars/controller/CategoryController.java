package com.myapi.cars.controller;

import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Category;
import com.myapi.cars.service.CategoryService;
import com.myapi.cars.service.CategoryService;
import com.myapi.cars.utility.PaginationSortingUtils;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
    }

    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO update(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
        return categoryService.update(categoryDTO, categoryId);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getById(@PathVariable Long categoryId) {
        return categoryService.findById(categoryId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DTOSearchResponse getAll(@RequestParam(defaultValue = "100") int limit,
                                    @RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PaginationSortingUtils.getPageable(limit, offset, sort);
        return categoryService.findAll(pageable);
    }

}

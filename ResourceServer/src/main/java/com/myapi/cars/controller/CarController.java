package com.myapi.cars.controller;

import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.service.CarService;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
@Slf4j
public class CarController {

    private final CarService carService;

    @Operation(summary = "Create the Car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Car already exists", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody CarDTO carDTO) {
        return carService.create(carDTO);
    }

    @Operation(summary = "Delete the Car by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Car deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)})
    @DeleteMapping("/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long carId) {
        carService.deleteById(carId);
    }

    @Operation(summary = "Update the Car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)})
    @PatchMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO update(@RequestBody CarDTO carDTO, @PathVariable Long carId) {
        return carService.update(carDTO, carId);
    }

    @Operation(summary = "Retrieve the Car by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)})
    @GetMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO getById(@PathVariable Long carId) {
        return carService.findById(carId);
    }

    @Operation(summary = "Retrieve the Cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cars retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class))})})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DTOSearchResponse getAll(@RequestParam(defaultValue = "100") int limit,
                                            @RequestParam(defaultValue = "0") int offset,
                                            @RequestParam(defaultValue = "id,asc") String[] sort,
                                            @RequestParam(required = false) String make,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) String model,
                                    @RequestParam(required = false) List<String> cars) {
        if (cars == null) cars = new ArrayList<>();
        Pageable pageable = PaginationSortingUtils.getPageable(limit, offset, sort);
        return carService.findAll(make, year, model, cars, pageable);
    }

}

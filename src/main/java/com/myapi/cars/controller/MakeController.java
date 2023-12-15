package com.myapi.cars.controller;

import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.service.MakeService;
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

@RestController
@RequestMapping("/api/v1/make")
@RequiredArgsConstructor
@Slf4j
public class MakeController {

    private final MakeService makeService;

    @Operation(summary = "Create the Make")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Make created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MakeDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Make already exists", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody MakeDTO makeDTO) {
        return makeService.create(makeDTO);
    }

    @Operation(summary = "Delete the Make by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Make deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MakeDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Make not found", content = @Content)})
    @DeleteMapping("/{makeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long makeId) {
        makeService.deleteById(makeId);
    }

    @Operation(summary = "Update the Make", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Make updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MakeDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Make not found", content = @Content)})
    @PatchMapping("/{makeId}")
    @ResponseStatus(HttpStatus.OK)
    public MakeDTO update(@RequestBody MakeDTO makeDTO, @PathVariable Long makeId) {
        return makeService.update(makeDTO, makeId);
    }

    @Operation(summary = "Retrieve the Make by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Make retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MakeDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Make not found", content = @Content)})
    @GetMapping("/{makeId}")
    @ResponseStatus(HttpStatus.OK)
    public MakeDTO getById(@PathVariable Long makeId) {
        return makeService.findById(makeId);
    }

    @Operation(summary = "Retrieve the Makes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Makes retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MakeDTO.class))})})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DTOSearchResponse getAll(@RequestParam(defaultValue = "100") int limit,
                                    @RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PaginationSortingUtils.getPageable(limit, offset, sort);
        return makeService.findAll(pageable);
    }

}
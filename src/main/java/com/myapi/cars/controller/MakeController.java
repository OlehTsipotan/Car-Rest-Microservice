package com.myapi.cars.controller;

import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.service.MakeService;
import com.myapi.cars.utility.PaginationSortingUtils;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody MakeDTO makeDTO) {
        return makeService.create(makeDTO);
    }

    @DeleteMapping("/{makeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long makeId) {
        makeService.deleteById(makeId);
    }

    @PatchMapping("/{makeId}")
    @ResponseStatus(HttpStatus.OK)
    public MakeDTO update(@RequestBody MakeDTO makeDTO, @PathVariable Long makeId) {
        return makeService.update(makeDTO, makeId);
    }

    @GetMapping("/{makeId}")
    @ResponseStatus(HttpStatus.OK)
    public MakeDTO getById(@PathVariable Long makeId) {
        return makeService.findByIdAsDTO(makeId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DTOSearchResponse getAll(@RequestParam(defaultValue = "100") int limit,
                                    @RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PaginationSortingUtils.getPageable(limit, offset, sort);
        return makeService.findAllAsDTO(pageable);
    }

}
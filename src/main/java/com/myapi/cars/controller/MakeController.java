package com.myapi.cars.controller;

import com.myapi.cars.model.Make;
import com.myapi.cars.service.MakeService;
import com.myapi.cars.utility.PaginationSortingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/make")
@RequiredArgsConstructor
@Slf4j
public class MakeController {

    private final MakeService makeService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Make Make) {
        Long id = makeService.create(Make);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{makeId}")
    public ResponseEntity<Void> delete(@PathVariable Long makeId) {
        makeService.deleteById(makeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Make> update(@RequestBody Make Make) {
        Make updatedMake = makeService.update(Make);
        return ResponseEntity.ok(updatedMake);
    }

    @GetMapping("/{makeId}")
    public ResponseEntity<Make> getById(@PathVariable Long makeId) {
        Make Make = makeService.findById(makeId);
        return ResponseEntity.ok(Make);
    }

    @GetMapping
    public ResponseEntity<List<Make>> getAll(@RequestParam(defaultValue = "100") int limit,
                                             @RequestParam(defaultValue = "0") int offset,
                                             @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PaginationSortingUtils.getPageable(limit, offset, sort);
        List<Make> categories = makeService.findAll(pageable);
        return ResponseEntity.ok(categories);
    }

}
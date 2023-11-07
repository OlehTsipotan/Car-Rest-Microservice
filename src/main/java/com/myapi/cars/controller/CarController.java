package com.myapi.cars.controller;

import com.myapi.cars.model.Car;
import com.myapi.cars.service.CarService;
import com.myapi.cars.utility.PaginationSortingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
@Slf4j
public class CarController {

    private final CarService CarService;

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody Car Car) {
        UUID id = CarService.create(Car);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> delete(@PathVariable UUID carId) {
        CarService.deleteById(carId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Car> update(@RequestBody Car Car) {
        Car updatedCar = CarService.update(Car);
        return ResponseEntity.ok(updatedCar);
    }

    @GetMapping("/{carId}")
    public ResponseEntity<Car> getById(@PathVariable UUID carId) {
        Car Car = CarService.findById(carId);
        return ResponseEntity.ok(Car);
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAll(@RequestParam(defaultValue = "100") int limit,
                                            @RequestParam(defaultValue = "0") int offset,
                                            @RequestParam(defaultValue = "id,asc") String[] sort,
                                            @RequestParam(required = false) String make,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) String model,
                                            @RequestParam(required = false) List<String> category) {
        if (category == null) category = new ArrayList<>();
        Pageable pageable = PaginationSortingUtils.getPageable(limit, offset, sort);
        List<Car> categories = CarService.findAll(make, year, model, category, pageable);
        return ResponseEntity.ok(categories);
    }

}

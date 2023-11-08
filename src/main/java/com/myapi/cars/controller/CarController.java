package com.myapi.cars.controller;

import com.myapi.cars.model.Car;
import com.myapi.cars.service.CarService;
import com.myapi.cars.utility.PaginationSortingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Long> create(@RequestBody Car car) {
        Long id = CarService.create(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> delete(@PathVariable Long carId) {
        CarService.deleteById(carId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Car> update(@RequestBody Car car) {
        Car updatedCar = CarService.update(car);
        return ResponseEntity.ok(updatedCar);
    }

    @GetMapping("/{carId}")
    public ResponseEntity<Car> getById(@PathVariable Long carId) {
        Car car = CarService.findById(carId);
        return ResponseEntity.ok(car);
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

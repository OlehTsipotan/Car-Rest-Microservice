package com.myapi.cars.controller;

import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.service.CarService;
import com.myapi.cars.utility.PaginationSortingUtils;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody CarDTO carDTO) {
        System.out.println("carDTO = " + carDTO);
        return carService.create(carDTO);
    }

    @DeleteMapping("/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long carId) {
        carService.deleteById(carId);
    }

    @PatchMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO update(@RequestBody CarDTO carDTO, @PathVariable Long carId) {
        return carService.update(carDTO, carId);
    }

    @GetMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO getById(@PathVariable Long carId) {
        return carService.findByIdAsDTO(carId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DTOSearchResponse getAll(@RequestParam(defaultValue = "100") int limit,
                                            @RequestParam(defaultValue = "0") int offset,
                                            @RequestParam(defaultValue = "id,asc") String[] sort,
                                            @RequestParam(required = false) String make,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) String model,
                                    @RequestParam(required = false) List<String> car) {
        if (car == null) car = new ArrayList<>();
        Pageable pageable = PaginationSortingUtils.getPageable(limit, offset, sort);
        return carService.findAllAsDTO(make, year, model, car, pageable);
    }

}

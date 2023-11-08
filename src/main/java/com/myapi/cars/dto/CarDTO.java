package com.myapi.cars.dto;

import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarDTO extends Dto {
    Long id;
    MakeDTO make;
    Integer year;
    String model;
    Set<CategoryDTO> categories;
}
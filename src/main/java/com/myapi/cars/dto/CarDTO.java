package com.myapi.cars.dto;

import jakarta.validation.constraints.Size;
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
    @Size(max = 255, message = "Model must be less than 255 characters")
    String model;
    Set<CategoryDTO> categories;
}
package com.myapi.cars.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO extends Dto {

    private Long id;

    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

}

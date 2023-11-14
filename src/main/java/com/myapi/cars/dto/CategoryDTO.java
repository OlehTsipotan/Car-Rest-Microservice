package com.myapi.cars.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO extends Dto {

    private Long id;

    private String name;

}

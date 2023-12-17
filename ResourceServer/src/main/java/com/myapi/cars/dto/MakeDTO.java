package com.myapi.cars.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MakeDTO extends Dto{

    private Long id;

    private String name;

}

package com.example.product_service.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDto {
    private String id;
    private String name;
    private Integer inventory;
}

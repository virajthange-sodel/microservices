package com.example.product_service.entitites;

import com.example.product_service.dtos.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@Table(name = "products_table")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer inventory;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private Integer userId;
}
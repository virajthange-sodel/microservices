package com.example.product_service.entitites;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private String password;
//    private List<Product> products;
}
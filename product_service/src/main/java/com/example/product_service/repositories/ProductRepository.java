package com.example.product_service.repositories;

import com.example.product_service.entitites.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public List<Product> findByUserId(Integer userId);
}

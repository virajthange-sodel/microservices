package com.example.user_service.services;

import com.example.user_service.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ProductClient {
    private final RestClient.Builder restClientBuilder;

    public Product getProduct(int productId) {
        return restClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/products/{id}", productId)
                .retrieve()
                .body(Product.class);
    }
}

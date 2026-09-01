package com.example.user_service.services;

import com.example.user_service.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductClient {
    private final RestClient.Builder restClientBuilder;

    public List<Product> getProducts(int id) {
        return restClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/products/byuser?userId={id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {});
    }
}

package com.example.product_service.controller;

import com.example.product_service.dtos.ProductDto;
import com.example.product_service.dtos.ProductStatus;
import com.example.product_service.entitites.Product;
import com.example.product_service.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
   private final ProductRepository productRepository;
   @Value("${server.port}")
   private String port;

   @PostMapping
   public ResponseEntity<Product> createProduct(@RequestBody ProductDto productdto) {

      System.out.println("Request came to instance running on port: "+ port);
      Product product = new Product();
//      BeanUtils.copyProperties(productdto, product);
//      product.setId(UUID.randomUUID().toString());
      product.setName(productdto.getName());
      product.setInventory(productdto.getInventory());
      product.setStatus(productdto.getStatus());
      product.setUserId(productdto.getUserId());
      System.out.println(product);
      Product save = productRepository.save(product);
      return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(save);
   }

   @GetMapping
   public ResponseEntity<List<Product>> getAllProducts() {
      return ResponseEntity.ok(
              new ArrayList<>(productRepository.findAll())
      );
   }

   @PostMapping("/cronjob")
   public void hitCronTask() {
      System.out.println("Cron job executed...!");
   }

   @GetMapping("/byuser")
   public List<Product> getProductsByUserId(@RequestParam Integer userId) {
      List<Product> byUserId = productRepository.findByUserId(userId);
      System.out.println(byUserId);
      return byUserId;

   }

   @GetMapping("/{id}")
   public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
      Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
      return ResponseEntity.ok(product);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
      Optional<Product> byId = productRepository.findById(id);
      if (byId.isEmpty()) {
         return ResponseEntity.notFound().build();
      }
      Product product = byId.get();
      if(product.getStatus() == ProductStatus.INACTIVE || product.getInventory() < 1) {
         throw new RuntimeException("Product is not active...!");
      }
      productRepository.deleteById(product.getId());
      return ResponseEntity.noContent().build();
   }
}
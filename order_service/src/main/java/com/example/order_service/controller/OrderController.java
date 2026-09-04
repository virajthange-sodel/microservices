package com.example.order_service.controller;

import com.example.order_service.entities.Order;
import com.example.order_service.entities.PStatus;
import com.example.order_service.entities.Product;
import com.example.order_service.entities.ProductStatus;
import com.example.order_service.repositories.OrderRepository;
import com.example.order_service.repositories.ProductStatusRepository;
import com.netflix.discovery.converters.Auto;
import jakarta.ws.rs.core.Response;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
//@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final RestClient.Builder restClientBuilder;
    private final ProductStatusRepository productStatusRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderController(
            OrderRepository orderRepository,
            ProductStatusRepository productStatusRepository,
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.productStatusRepository = productStatusRepository;
        this.restClientBuilder = restClientBuilder;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
//        Product product = restClientBuilder.build()
//                .get()
//                .uri("http://PRODUCT-SERVICE/api/products/{id}", order.getProductId())
////                .uri("http://localhost:8080/api/products/{id}", order.getProductId())
//                .retrieve()
//                .body(Product.class);

        ResponseEntity<Product> product = restClientBuilder.build()
                .get()
                .uri("http://PRODUCT-SERVICE/api/products/{id}", order.getProductId())
                .retrieve()
                .toEntity(Product.class);

        System.out.println(product);
        System.out.println(product.getStatusCode());
        System.out.println(product.getBody());
        System.out.println(product.getClass());

//        if(product.getStatusCode().value() == 200) {
        if (product.getStatusCode() == HttpStatus.OK && product.getBody().getInventory() > 0) {
            System.out.println("Product available");
            ProductStatus productStatus = new ProductStatus();
            productStatus.setProductId(product.getBody().getId());
            productStatus.setStatus(PStatus.AVAILABLE.toString());
            productStatusRepository.save(productStatus);
        } else {
            System.out.println("Product not available");
            ProductStatus productStatus = new ProductStatus();
            productStatus.setProductId(product.getBody().getId());
            productStatus.setStatus(PStatus.NOTAVAILABLE.name());
            productStatusRepository.save(productStatus);
        }

        Order savedOrder = orderRepository.save(order);

//        kafkaTemplate.send("users-topic", "Order created");
//        if you don't choose the partition yourself. The producer's partitioner determines where the record goes.
//        With Kafka's default behavior, keyless records are generally distributed across partitions rather than permanently selecting one partition.

//            kafkaTemplate.send("users-topic",  "Kafka key", "Order created");       //Kafka uses the key to select a partition.

        if (savedOrder.getProductId() < 5) {
            kafkaTemplate.send("users-topic", 0, "Kafka key", "Order created");
        } else {

            kafkaTemplate.send("users-topic", 1, "Kafka key", "Order created");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedOrder);
    }


    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {

        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        orderRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/hitpostproduct")
    public ResponseEntity<Product> hitPostCreateProduct(@RequestBody Product product) {
        ResponseEntity<Product> entity = restClientBuilder.build()
                .post()
                .uri("http://PRODUCT-SERVICE/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(product)
                .retrieve()
//                .onStatus(HttpStatusCode::is5xxServerError, (request, response)-> {
//                    throw new RuntimeException("Eroor at server side...");
//                })
                .toEntity(Product.class);
        System.out.println(entity);
//        if(entity.getStatusCode().value() == 201) {
        if (entity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(200).body(product);
        } else {
            return ResponseEntity.status(500).body(null);
        }
    }
}
package com.example.order_service.controller;

import com.example.order_service.entities.Order;
import com.example.order_service.entities.Product;
import com.example.order_service.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final RestClient restClient;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Product product = restClient.get()
                .uri("/api/products/{id}", order.getProductId())
                .retrieve()
                .body(Product.class);

        System.out.println(product);
        Order savedOrder = orderRepository.save(order);

        kafkaTemplate.send("order-counter", "Order created");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedOrder);
    }


    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        return ResponseEntity.ok(orders);
    }
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(
//            @PathVariable Integer id
//    ) {
//
//        return orderRepository
//    }


//    @PutMapping("/{id}")
//    public ResponseEntity<Order> updateOrder(
//            @PathVariable Integer id,
//            @RequestBody Order order
//    ) {
//
//        return orderRepository.findById(id)
//                .map(existingOrder -> {
//
//                    existingOrder.setProductId(order.getProductId());
//                    existingOrder.setQuantity(order.getQuantity());
//
//                    Order updatedOrder =
//                            orderRepository.save(existingOrder);
//
//                    return ResponseEntity.ok(updatedOrder);
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {

        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        orderRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

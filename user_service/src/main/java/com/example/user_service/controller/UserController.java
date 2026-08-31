package com.example.user_service.controller;

import com.example.user_service.entities.Product;
import com.example.user_service.entities.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RestClient.Builder restClientBuilder;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(201).body(userRepository.save(user));
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}/products")
    public List<Product> getUserWithProducts(@PathVariable Integer id) {
        System.out.println("Hitting user-products api");
        Optional<User> byId = userRepository.findById(id);
        if(byId.isEmpty()) {
            throw new RuntimeException("User not found...!");
        }
        User user = byId.get();

        return restClientBuilder.build().get()
                .uri("http://localhost:8080/api/products/byuser?userId={id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {});
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found...!")));
    }

//    second minute hour day-of-month month day-of-week
//    @Scheduled(cron = "*/5 * * * * *")
//    @Scheduled(cron = "*/5 * * * * MON-FRI")
//    @Scheduled(cron = "*/5 10/5 1 1-15 AUG MON-FRI")
//    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
//    @Scheduled(initialDelay = 2000,fixedRate = 5000)
    @Async("cronsExecutor")
    public void scheduleTask() {
        System.out.println("Thread name is: "+Thread.currentThread().getName());
        System.out.println("This is scheduled task "+ LocalDateTime.now());
    }

//    @Scheduled(cron = "*/5 * * * * *")
    @Async("emailExecutor")
    public void scheduleEmail() {
        System.out.println("Theread name: "+ Thread.currentThread().getName());
        System.out.println("Sending email...");
    }



}
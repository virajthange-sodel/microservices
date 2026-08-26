package com.example.order_counter_service.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCounterConsumer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "order-counter", groupId = "counter-group")
    public void consumeOrderCounter(String msg) {
        String count = redisTemplate.opsForValue().get("order-counter");
        if(count == null) {
            redisTemplate.opsForValue().set("order-counter", "1");
        }else{
            int updatedCount = Integer.parseInt(count) + 1;
            System.out.println("Updated counter is: "+ updatedCount);
            redisTemplate.opsForValue()
                    .set("order-counter", String.valueOf(updatedCount));
        }
        System.out.println("Consumed message is: "+ msg);
    }
}

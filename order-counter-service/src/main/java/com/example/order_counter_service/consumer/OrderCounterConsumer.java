package com.example.order_counter_service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCounterConsumer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "users-topic", groupId = "counter-group")
    public void consumeOrderCounter(ConsumerRecord<String, String> msg) {
        String count = redisTemplate.opsForValue().get("users-topic");
        if(count == null) {
            redisTemplate.opsForValue().set("users-topic", "1");
        }else{
            int updatedCount = Integer.parseInt(count) + 1;
            System.out.println("Updated counter is: "+ updatedCount);
            redisTemplate.opsForValue()
                    .set("users-topic", String.valueOf(updatedCount));
        }
        System.out.println("Consumed message is: "+ msg);
        System.out.println("Mesage key is: "+msg.key());
        System.out.println("Mesage topic is: "+msg.topic());
        System.out.println("Mesage offset is: "+msg.offset());
        System.out.println("Mesage value is: "+msg.value());
        System.out.println("Mesage partition is: "+msg.partition());
        System.out.println("Mesage timestamp is: "+msg.timestamp());
    }
}
package com.example.user_service.configs;

import org.springframework.boot.restclient.autoconfigure.RestClientBuilderConfigurer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
//    @Bean
//    @LoadBalanced
//    public RestClient.Builder loadBalancedRestClientBuilder() {
//        return RestClient.builder();
//    }
//    @Bean
//    @Primary
//    public RestClient.Builder restClientBuilder() {
//        return RestClient.builder();
//    }


    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder(
            RestClientBuilderConfigurer configurer) {

        return configurer.configure(RestClient.builder());
    }

    @Bean
    @Primary
    public RestClient.Builder restClientBuilder(
            RestClientBuilderConfigurer configurer) {

        return configurer.configure(RestClient.builder());
    }
}
package com.example.user_service.configurations;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class QuartzConfig {
    @Bean
    public SpringBeanJobFactory springBeanJobFactory(
            ApplicationContext applicationContext) {

        SpringBeanJobFactory jobFactory =
                new SpringBeanJobFactory();

        jobFactory.setApplicationContext(applicationContext);

        return jobFactory;
    }
}

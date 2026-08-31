package com.example.user_service.schedulars;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShcedularConfigs {
    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder
                .newJob(MyQuartzJob.class)
                .withIdentity("myJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger myTrigger(JobDetail myJobDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(myJobDetail)
                .withIdentity("myTrigger")
                .withSchedule(
                        CronScheduleBuilder
                                .cronSchedule("*/5 * * * * ?")
                )
//                .withSchedule(
//                        SimpleScheduleBuilder
//                                .simpleSchedule()
//                                .withIntervalInSeconds(10)
////                                .withRepeatCount(5)      //limited repititions
//                                .repeatForever()
//                )
                .build();
    }



    @Bean
    public JobDetail retryJob() {
        return JobBuilder
                .newJob(MyQuartzJob.class)
                .withIdentity("retryJob")
                .storeDurably()
                .build();
    }
    @Bean
    public Trigger retryJobTrigger(JobDetail myJobDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(myJobDetail)
                .withIdentity("retry-job-trigger")
                .usingJobData("attempt", 1)
                .build();
    }
}
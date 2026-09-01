package com.example.user_service.schedulars;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShcedularConfigs {
    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder
                .newJob(MyQuartzJob.class)
                .withIdentity("myJob")
                .storeDurably()         //Generally the job should have trigger,but we are not assigning any trigger here. With this method Quartz can store, even if there is currently no trigger attached to it.
                .build();
    }

    @Bean
    public Trigger myTrigger(@Qualifier("myJobDetail") JobDetail jobDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(jobDetail)
                .withIdentity("myTrigger")
                .withSchedule(
                        CronScheduleBuilder
                                .cronSchedule("*/5 * * * * ?")
//                                .withMisfireHandlingInstructionFireAndProceed()
                                .withMisfireHandlingInstructionDoNothing()
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



//    @Bean
//    public JobDetail retryJob() {
//        return JobBuilder
//                .newJob(ProductRetryJob.class)
//                .withIdentity("retryJob")
//                .storeDurably()
//                .build();
//    }
//    @Bean
//    public Trigger retryJobTrigger(JobDetail myJobDetail) {
//        return TriggerBuilder
//                .newTrigger()
//                .forJob(myJobDetail)
//                .withIdentity("retry-job-trigger")
//                .usingJobData("attempt", 1)
//                .build();
//    }
}
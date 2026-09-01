package com.example.user_service.services;

import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuartzServices {
    private final Scheduler scheduler;

    public void pauseJob()  {
        System.out.println("Job is paused");
        try {
            scheduler.pauseTrigger(
                    TriggerKey.triggerKey("myTrigger")
            );
        }catch (SchedulerException e) {
            System.out.println("Handled schedular exception");
        }
    }

    public void resumeJob() {
        System.out.println("Resuming job");
        try {
            scheduler.resumeTrigger(
                    TriggerKey.triggerKey("mytrigger")
            );
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}

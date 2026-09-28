package com.example.user_service.schedulars;

import com.example.user_service.services.ProductClient;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRetryJob implements Job {
    private final Scheduler scheduler;
    private final ProductClient productClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int attempt = context.getMergedJobDataMap().getInt("attempt");
        int userId = context.getMergedJobDataMap().getInt("userId");

        System.out.println("Fetching product " + userId + ", attempt " + attempt);

        try {
            productClient.getProducts(userId);
            System.out.println("Product fetched successfully.");
        } catch (Exception e) {
            System.out.println("Product fetch failed.");
            if (attempt < 3) {scheduleNextRetry(userId, attempt + 1);
            } else {
                System.out.println("3 attempts completed. " + "No more retries.");
            }
        }
    }

    public void scheduleNextRetry(int userId, int nextAttempt) throws JobExecutionException {
        try {
            JobKey jobKey = new JobKey("productRetryJob-" + userId + "-" + nextAttempt);

            if (scheduler.checkExists(jobKey)) {
                System.out.println("Retry job already exists: " + jobKey);
                return;
            }
            JobDetail jobDetail = JobBuilder
                            .newJob(ProductRetryJob.class)
                            .withIdentity(
                                    "productRetryJob-" + userId + "-" + nextAttempt)
                            .usingJobData("userId", userId)
                            .usingJobData("attempt", nextAttempt
                            )
                            .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity(
                                    "productRetryTrigger-" + userId + "-" + nextAttempt
                            )
                            .startAt(
                                    DateBuilder.futureDate(10, DateBuilder.IntervalUnit.SECOND)
                            )
                            .forJob(jobDetail)
                            .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new JobExecutionException(e);
        }
    }
}
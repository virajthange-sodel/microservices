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
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        int attempt =
                context.getMergedJobDataMap()
                        .getInt("attempt");

        int productId =
                context.getMergedJobDataMap()
                        .getInt("productId");

        System.out.println(
                "Fetching product " +
                        productId +
                        ", attempt " +
                        attempt
        );

        try {

            productClient.getProduct(productId);

            System.out.println(
                    "Product fetched successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Product fetch failed."
            );

            if (attempt < 3) {
                scheduleNextRetry(
                        productId,
                        attempt + 1
                );
            } else {
                System.out.println(
                        "3 attempts completed. " +
                                "No more retries."
                );
            }
        }
    }

    private void scheduleNextRetry(
            int productId,
            int nextAttempt)
            throws JobExecutionException {

        try {

            JobDetail jobDetail =
                    JobBuilder.newJob(ProductRetryJob.class)
                            .withIdentity(
                                    "productRetryJob-" +
                                            productId + "-" +
                                            nextAttempt
                            )
                            .usingJobData(
                                    "productId",
                                    productId
                            )
                            .usingJobData(
                                    "attempt",
                                    nextAttempt
                            )
                            .build();

            Trigger trigger =
                    TriggerBuilder.newTrigger()
                            .withIdentity(
                                    "productRetryTrigger-" +
                                            productId + "-" +
                                            nextAttempt
                            )
                            .startAt(
                                    DateBuilder.futureDate(
                                            10,
                                            DateBuilder.IntervalUnit.SECOND
                                    )
                            )
                            .forJob(jobDetail)
                            .build();

            scheduler.scheduleJob(
                    jobDetail,
                    trigger
            );

        } catch (SchedulerException e) {
            throw new JobExecutionException(e);
        }
    }
}
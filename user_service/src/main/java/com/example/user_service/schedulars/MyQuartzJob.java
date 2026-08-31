package com.example.user_service.schedulars;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyQuartzJob implements Job {

    private Logger logger = LoggerFactory.getLogger(MyQuartzJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(context);
        logger.info("This is from logger...");
        System.out.println("This is a quartz job scheduling method...!");
    }
}
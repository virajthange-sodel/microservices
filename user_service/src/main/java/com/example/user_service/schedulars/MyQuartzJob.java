package com.example.user_service.schedulars;

import com.example.user_service.entities.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyQuartzJob implements Job {

    private Logger logger = LoggerFactory.getLogger(MyQuartzJob.class);
    private final UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        System.out.println(context);
        logger.warn("This is quartz scheduling job");

        List<User> all = userRepository.findAll();
        for(User user : all) {
            System.out.println("User is: "+ user.getId()+ " " +user.getName());
            System.out.println("Executed by "+ Thread.currentThread().getName());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
//        System.out.println("This is a quartz job scheduling method...!");
    }
}
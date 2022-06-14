package com.netvlab.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class testTask {
    /*@Scheduled(cron = "0/5 * * * * ?")*/
    public void doTask(){
        System.out.println("task.....................");
    }
}

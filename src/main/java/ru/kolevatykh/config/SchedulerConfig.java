package ru.kolevatykh.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

@Slf4j
public class SchedulerConfig {

    public static void scheduleLimitUpdate() {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        try {
            Scheduler sched = schedFact.getScheduler();

            JobDetail job =
                    JobBuilder.newJob(LimitUpdateJob.class)
                    .withIdentity("limitUpdateJob", "group1")
                    .build();

            Trigger trigger =
                    TriggerBuilder.newTrigger()
                            .withIdentity("myTrigger", "group1")
                            .startNow()
                            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                            .withIntervalInSeconds(10)
                                            .repeatForever())
                            .build();

            sched.scheduleJob(job, trigger);
            sched.start();
        } catch (SchedulerException e) {
            log.debug(e.getMessage());
        }
    }
}

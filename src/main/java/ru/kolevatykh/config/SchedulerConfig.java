package ru.kolevatykh.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

@Slf4j
public class SchedulerConfig {

    /**
     * Setting up a scheduler for traffic limits' values updating from DB every 20 minutes
     */
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
                            .withIdentity("limitUpdate", "group1")
                            .startNow()
                            .withSchedule(
                                    SimpleScheduleBuilder.simpleSchedule()
                                            .withIntervalInMinutes(20)
                                            .repeatForever())
                            .build();

            sched.scheduleJob(job, trigger);
            sched.start();
        } catch (SchedulerException e) {
            log.debug(e.getMessage());
        }
    }
}

package ru.kolevatykh.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import ru.kolevatykh.service.TrafficLimitsService;

@Slf4j
public class LimitUpdateJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        TrafficLimitsService.limitsUpdate();
    }
}

package ru.kolevatykh;

import lombok.extern.slf4j.Slf4j;
import ru.kolevatykh.config.KafkaConfig;
import ru.kolevatykh.config.PcapConfig;
import ru.kolevatykh.config.SchedulerConfig;
import ru.kolevatykh.entity.LimitsPerHour;
import ru.kolevatykh.service.DatabaseService;
import ru.kolevatykh.service.TrafficLimitsService;

import java.io.IOException;
import java.time.OffsetDateTime;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException {

        DatabaseService db = new DatabaseService();
        try {
            db.persist(new LimitsPerHour("min", 10246, OffsetDateTime.now()));
            db.persist(new LimitsPerHour("max", 1073741824, OffsetDateTime.now()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // Create new kafka topic if it doesn't exist
        KafkaConfig.createTopic();

        // Schedule a job for limits updating
        SchedulerConfig.scheduleLimitUpdate();

        // Gets all devices
        Integer devices = PcapConfig.getNetworkDevices().size();

        TrafficLimitsService trafficLimitsService = new TrafficLimitsService();
        trafficLimitsService.setDevices(devices);
        if (args.length > 0) trafficLimitsService.setIp(args[0]);
        try {
            trafficLimitsService.captureTraffic();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
}

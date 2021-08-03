package ru.kolevatykh;

import lombok.extern.slf4j.Slf4j;
import ru.kolevatykh.config.KafkaConfig;
import ru.kolevatykh.config.PcapConfig;
import ru.kolevatykh.config.SchedulerConfig;
import ru.kolevatykh.entity.LimitsPerHour;
import ru.kolevatykh.service.DatabaseService;
import ru.kolevatykh.service.TrafficLimitsService;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
public class App {

    public static void main(String[] args) {

        initDb();

        KafkaConfig.createTopic("alerts");

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

    /**
     * Persist traffic limits values in DB, if table is empty
     */
    private static void initDb() {
        DatabaseService db = new DatabaseService();
        List<LimitsPerHour> limits = db.findAll();
        try {
            if (limits.isEmpty()) {
                db.persist(new LimitsPerHour("min", 1024, OffsetDateTime.now()));
                db.persist(new LimitsPerHour("max", 1073741824, OffsetDateTime.now()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

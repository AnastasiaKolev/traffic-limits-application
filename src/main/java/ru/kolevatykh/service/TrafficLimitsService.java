package ru.kolevatykh.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import ru.kolevatykh.config.KafkaConfig;
import ru.kolevatykh.config.SparkConfig;

@Slf4j
@Data
public class TrafficLimitsService {

    private static Integer max;
    private static Integer min;
    private Integer devices;
    private String ip;

    private static DatabaseService databaseService;

    public void captureTraffic() throws Exception {
        // Create a local StreamingContext with * working threads and batch interval of 5 minutes
        JavaStreamingContext javaStreamingContext = new SparkConfig().javaStreamingContext();

        limitsUpdate();

        JavaDStream<Integer> union = null;
        for (int deviceId = 0; deviceId < devices; deviceId++) {
            JavaDStream<Integer> dStream = javaStreamingContext.receiverStream(new PcapReceiver(ip, deviceId));
            union = javaStreamingContext.union(dStream);
        }

        if (union != null) {
            JavaDStream<Integer> packets = union.reduce((Function2<Integer, Integer, Integer>) Integer::sum);

            packets.foreachRDD(new VoidFunction<JavaRDD<Integer>>() {
                @Override
                public void call(JavaRDD<Integer> packet) {
                    if (!packet.isEmpty()) {
                        Integer packetLength = packet.first();
                        log.warn(">>>>>>>>>>>>>>>>>>>>>>> NEW: " + packetLength);
                        if (packetLength < min || packetLength > max) {
                            String msg = "The packets length captured in 5 minutes: " + packetLength
                                    + " exceeds the limits min = " + min + " and max = " + max;
                            sendAlert(msg);
                        }
                    }
                }

            });
            packets.print();
        }
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }

    public static void limitsUpdate() {
        databaseService = new DatabaseService();
        max = databaseService.findLimitByNameAndDate("max");
        min = databaseService.findLimitByNameAndDate("min");
        log.info("Limits are updated: max = {}, min = {}", max, min);
    }


    public void sendAlert(String message) {
        KafkaConfig.sendMessage(message);
    }
}

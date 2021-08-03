package ru.kolevatykh.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Slf4j
public class KafkaConfig {

    private static final String TOPIC_NAME = "alerts";
    private static final String bootstrapServers = "127.0.0.1:9092";
    private static final KafkaProducer<String, String> kafkaProducer = getKafkaProducer();

    /**
     * Create a new Kafka topic if it doesn't exist
     */
    public static void createTopic(String name) {
        String topicName = TOPIC_NAME;
        if (name != null) topicName = name;
        try (Admin admin = Admin.create(getProperties())) {
            int partitions = 1;
            short replicationFactor = 1;
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);

            CreateTopicsResult result = admin.createTopics(Collections.singleton(newTopic));
            // get the async result for the new topic creation
            KafkaFuture<Void> future = result.values().get(topicName);
            // call get() to block until topic creation has completed or failed
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            log.debug(e.getMessage());
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ACKS_CONFIG, "all");
        properties.put(RETRIES_CONFIG, 0);
        properties.put(LINGER_MS_CONFIG, 1);
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    public static KafkaProducer<String, String> getKafkaProducer() {
        return new KafkaProducer<>(getProperties());
    }

    public static void sendMessage(String message) {
        try (Producer<String, String> producer = kafkaProducer) {
            OffsetDateTime dateTime = OffsetDateTime.now();
            log.info("dateTime = {}, message = {}", dateTime, message);
            producer.send(new ProducerRecord<>(TOPIC_NAME, dateTime.toString(), message));
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
}

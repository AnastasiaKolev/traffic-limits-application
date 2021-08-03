//package ru.kolevatykh;
//
//
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.junit.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//import ru.kolevatykh.config.KafkaConfig;
//
//import java.util.Properties;
//
//import static org.apache.kafka.clients.producer.ProducerConfig.*;
//
///**
// * Unit test for simple App.
// */
//@Testcontainers
//public class AppTest {
//    /**
//     * Rigorous Test :-)
//     */
//    @Test
//    public void shouldAnswerWithTrue() {
//        Assert.assertTrue(true);
//    }
//
//    @Container
//    private static final KafkaContainer KAFKA_CONTAINER =
//            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
//
//
//    @BeforeEach
//    void setup() {
//        KafkaConfig.createTopic("test-topic");
//    }
//
//
//    @ClassRule
//    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
//
//    @Test
//    public void givenTopicName_whenCreateNewTopic_thenTopicIsCreated() throws Exception {
//        String topicCommand = "/usr/bin/kafka-topics --bootstrap-server=localhost:9092 --list";
//        String stdout = KAFKA_CONTAINER.execInContainer("/bin/sh", "-c", topicCommand)
//                .getStdout();
//
//        assert (stdout).contains("test-topic");
//    }
//
//    private static KafkaProducer<String, String> createKafkaProducer() {
//        Properties props = new Properties();
//        props.put(BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
//        props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//
//        return new KafkaProducer<>(props);
//    }
//}

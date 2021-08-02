//package ru.kolevatykh;
//
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.junit.*;
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
//    private KafkaConfig kafkaTopicApplication;
//
//    private KafkaProducer<String, String> producer = createKafkaProducer();
//    private Properties streamsConfiguration = new Properties();
//
//    static final String TEXT_LINES_TOPIC = "TextLinesTopic";
//
//    private final String TEXT_EXAMPLE_1 = "test test and test";
//    private final String TEXT_EXAMPLE_2 = "test filter filter this sentence";
//
//    @ClassRule
//    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
//
//    @Test
//    public void givenTopicName_whenCreateNewTopic_thenTopicIsCreated() throws Exception {
//        new KafkaConfig().createTopic();
//
//        String topicCommand = "/usr/bin/kafka-topics --bootstrap-server=localhost:9092 --list";
//        String stdout = KAFKA_CONTAINER.execInContainer("/bin/sh", "-c", topicCommand)
//                .getStdout();
//
//        assert (stdout).contains("alerts");
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

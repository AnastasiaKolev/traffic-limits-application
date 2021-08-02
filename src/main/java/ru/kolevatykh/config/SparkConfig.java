package ru.kolevatykh.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

@Data
@AllArgsConstructor
public class SparkConfig {

    private static final String APP_NAME = "TrafficLimits";
    private static final Integer duration = 5;

    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName(APP_NAME)
                .setMaster("local[*]")
                .set("spark.testing.memory", "471859200");
    }

    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

    public JavaStreamingContext javaStreamingContext() {
        return new JavaStreamingContext(javaSparkContext(), Durations.seconds(Long.valueOf(duration)));
    }

}

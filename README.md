## Traffic Limits App

This application is designed for capturing and calculation network traffic specified by port/IP address in 5 minutes intervals.
It sends messages about exceeding the limits for traffic to Kafka.
Traffic limits are updating every 20 minutes by a scheduled job.

### How to run the application by steps:

#### 1. Setting up the system variables:
* Create JAVA_HOME: C:\Program Files\Java\jdk-11 and add to PATH: %JAVA_HOME%\bin;
* Create M2_HOME: C:\Program Files\Maven\apache-maven-3.8.1 and add it to PATH: %M2_HOME%\bin;
* Create SPARK_HOME: C:\spark-3.1.2-bin-hadoop3.2 and add to PATH: %SPARK_HOME%\bin;
* Create SPARK_LOCAL_IP=localhost
* Create variable HADOOP_HOME: C:\Program Files\Hadoop\hadoop-3.0.0 and add to PATH: %HADOOP_HOME%\bin;
   The most important part of the Hadoop PATH is winutils.exe, check the file in the \bin

#### 2. Running the Docker:
   ```
   docker-compose up -d
   ```

#### 3. Setting up the database:
1. Create schema with  the command:
   ```
   create schema traffic_limits;
   alter schema traffic_limits owner to "user";
   ```
2. Inserting random data directly into the DB:
   ```
   insert into traffic_limits.limits_per_hour (id, effective_date, limit_name, limit_value) 
   values (gen_random_uuid(), now() at time zone '-3:00', 'max' , 1073741824);
   
   insert into traffic_limits.limits_per_hour (id, effective_date, limit_name, limit_value) 
   values (gen_random_uuid(), now() at time zone '-3:00', 'min', 1024);
   ```
3. Check the database rows
   ```
   select * from traffic_limits.limits_per_hour;
   ```

#### 4. Run the application from IDE:
   ```
   mvn clean install
   
   Run the application with optional args[0] parameter, which stands for IP address
   ```

#### 4.1 Run the application using terminal:
   ```
   mvn clean install assembly:single
   
   spark-submit --class ru.kolevatykh.App target\traffic-limits-application-1.0.0-jar-with-dependencies.jar ${IP}(optional) 
   ```

#### 5 Check the online data using SparkUI

SparkUI: Bound SparkUI to localhost, and started at http://kubernetes.docker.internal:4040

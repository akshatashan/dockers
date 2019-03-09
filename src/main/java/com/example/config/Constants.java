package com.example.config;

public final class Constants {
   //Hide the constructor
    private Constants(){}

    public static final String READ_TIMESTAMP_FORMAT = "yyyy-MM-dd['T']HH:mm:ss.SSSSSS[ ][Z]";

    // testing settings
    // Use "docker.for.mac.host.internal" if running this locally on MacOS.
    public static final String KAFKA_SERVER = "kafka:9093"; // Replace with the relevant IP address if using cloud setup.
    public static final String KAFKA_TOPIC_READ = "input";
    public static final String KAFKA_TOPIC_WRITE = "output";
    public static final Integer WINDOW_SIZE = 1;
    public static final Integer WINDOW_LATENESS = 10;
}

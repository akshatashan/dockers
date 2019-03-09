package com.example.io;

import com.example.messages.Message;

import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.io.jdbc.JdbcIO.PreparedStatementSetter;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

/**
 * Write messages into a topic in Kafka and/or a database and end the pipeline.
 */
public class Write extends PTransform<PCollection<Message>, PDone> {
    private String topic;
    private String kafka_server;

    public PTransform<PCollection<String>, PDone> WriteToTopic;
    DoFn<Message, String> converter = new Message.MessageToString();

    public Write(String kafka_server, String kafka_topic) {
        this.kafka_server = kafka_server;
        this.topic = kafka_topic;
    }

    @Override
    public PDone expand(PCollection<Message> p) {
        final Logger logger = Logger.getLogger(Write.class);
            WriteToTopic = KafkaIO.<Void, String>write()
                .withBootstrapServers(kafka_server)
                .withTopic(topic)
                .withValueSerializer(StringSerializer.class) // just need serializer for value
                .values();

            return p.apply("MessageToString", ParDo.of(this.converter))
                    .apply("WriteToTopic", WriteToTopic);
    }
}

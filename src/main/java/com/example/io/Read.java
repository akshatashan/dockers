package com.example.io;

import com.example.config.Constants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

/**
 * Begin a pipeline by reading a topic in Kafka.
 */
public class Read extends PTransform<PBegin, PCollection<String>> {
    protected String topic;
    protected String server;
    protected boolean parseEventTime = false;

    public Read(String server, String topic) {
        this.server = server;
        this.topic = topic;
    }

    private class ReadFromTopic extends DoFn<KV<Long, String>, String> {
        @ProcessElement
        public void processElement(ProcessContext processContext) throws Exception {
            final Logger logger = Logger.getLogger(ReadFromTopic.class);
            KV<Long, String> input = processContext.element();
            //logger.info("input is " + input);
            processContext.output(input.getValue());
        }
    }


    @Override
    public PCollection<String> expand(PBegin p) {
        KafkaIO.Read<Long, String> kafkaReader = KafkaIO.<Long, String>read()
            .withBootstrapServers(server)
            .withTopic(topic)
            .withKeyDeserializer(LongDeserializer.class)
            .withValueDeserializer(StringDeserializer.class)
            .updateConsumerProperties(ImmutableMap.of("enable.auto.commit", "true"))
            .updateConsumerProperties(ImmutableMap.of("group.id", "Consumer1")).commitOffsetsInFinalize();

        PTransform<PBegin, PCollection<KV<Long, String>>> setupKafkaRead = kafkaReader.withoutMetadata();

        return p
            .apply("SetupKafkaRead", setupKafkaRead)
            .apply("ReadFromTopic", ParDo.of(new ReadFromTopic()));
    }
}

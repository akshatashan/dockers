package com.example.pipelines;

import com.example.config.Constants;
import com.example.io.Read;
import com.example.io.Write;
import com.example.messages.Input;
import com.example.messages.Message;
import com.example.transforms.ParseMessage;
import com.example.transforms.SetTimestampFn;
import com.example.transforms.WindowSize;
import com.example.windows.CustomWindow;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.WithTimestamps;

/**
 *
 * The pipeline reads from a topic in Kafka, parses the message into Input,
 * and outputs the maximal value in a fixed window and writes Output to Kafka topic.
 */
public class CountPipeline {
    public interface RunnerOptions extends PipelineOptions {

    }
    public static void main(String[] args) {
        RunnerOptions runnerOptions = PipelineOptionsFactory.fromArgs(args).withValidation().as(RunnerOptions.class);
        Pipeline p = Pipeline.create(runnerOptions);
        
        // main steps
        p
            .apply("Read", new Read(Constants.KAFKA_SERVER, Constants.KAFKA_TOPIC_READ))
            .apply("Parse", new ParseMessage(Input.class))
            .apply("Timestamping", WithTimestamps.of(new SetTimestampFn()))
            .apply("Windowing", CustomWindow.fixedWindow(Constants.WINDOW_SIZE,Constants.WINDOW_LATENESS))
            .apply("Counts", Combine.globally(Count.<Message>combineFn()).withoutDefaults())
            .apply("WindowSize", ParDo.of(new WindowSize()))
            .apply("Write", new Write(Constants.KAFKA_SERVER,Constants.KAFKA_TOPIC_WRITE));

        p.run().waitUntilFinish();
    }
}

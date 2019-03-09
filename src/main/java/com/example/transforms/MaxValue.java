package com.example.transforms;

import com.example.messages.Message;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Max;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.log4j.Logger;

/**
 * A transform that outputs the maximal message of the input. The comparison
 * between messages must be defined in the message types.
 */
public class MaxValue extends PTransform<PCollection<Message>, PCollection<Message>> {

    @Override
    public PCollection<Message> expand(PCollection<Message> input) {
        final Logger logger = Logger.getLogger(MaxValue.class);

        PCollection<Message> output = input.apply("FindMax", Max.<Message>globally().withoutDefaults());
    

        logger.info(String.format("******Message(%s) is picked by %s.", output.toString(), this.getClass().getSimpleName()));

        return output;
    }
}

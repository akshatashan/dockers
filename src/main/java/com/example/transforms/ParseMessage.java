package com.example.transforms;

import com.example.messages.Message;
import com.example.messages.Message.MessageFromString;

import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

/**
 * A transform that converts String messages into the abstract data type Message
 */

public class ParseMessage extends PTransform<PCollection<String>, PCollection<Message>> {
    Class<Message> c;

    @SuppressWarnings("unchecked")
    public ParseMessage(Class<? extends Message> c) {
        this.c = (Class<Message>)c;
    }

    @Override
    public PCollection<Message> expand(PCollection<String> p) {
        return p
            .apply("Parse", ParDo.of(new MessageFromString(this.c)));
    }
}

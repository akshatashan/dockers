package com.example.transforms;

import com.example.messages.Input;
import com.example.messages.Message;

import org.apache.beam.sdk.transforms.SerializableFunction;
import org.joda.time.Instant;

public class SetTimestampFn implements SerializableFunction<Message, Instant> {
    @Override
    public Instant apply(Message input) {
      try {
        Input x = (Input) input;
        return new Instant(x.getTimestamp());
      } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
        return Instant.now();
      }
    }
  }
  
package com.example.transforms;

import com.example.messages.Input;
import com.example.messages.Message;
import com.example.messages.Out;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.log4j.Logger;

public class WindowSize extends DoFn<Long, Message> {
    @ProcessElement
    public void processElement(@Element Long message, BoundedWindow window, OutputReceiver<Message> out) {
      final Logger logger = Logger.getLogger(WindowSize.class);
      String s = window.maxTimestamp().toString();       
      Out o = new Out("", message, s);
        out.output(o);
      }
}
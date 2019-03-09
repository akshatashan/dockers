package com.example.messages;

import java.io.Serializable;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.log4j.Logger;

/**
 * This class is the base of all messages that the transforms can apply to. All other classes of messages should inherit this class.
 */
public abstract class Message implements Serializable, Comparable<Message> {
    protected String identifier;

    // JSON pointers in the input schema with default values
    protected String identifierKey = "/id";
    

    public Message() {
    }

    public Message(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * overwrite these methods if transforms are supported
     */
    public int compareTo(Message t) {
        throw(new UnsupportedOperationException());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;

        Message m = (Message)o;
        if (!Objects.equals(identifier, m.identifier))
            return false;

        return true;
    }

    /**
     * This must be implemented in child classes to convert a string into to create a new object.
     */
    public abstract Message fromString(String s);

    /**
     * Wrapper to get an object from a string. Requires custom-defined de-serializer.
     */
    protected Message fromStringDefault(String s, Class<? extends Message> c, StdDeserializer des) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("CustomDeserializer", 
                    new Version(1, 0, 0, null, null, null));
            module.addDeserializer(c, des);
            mapper.registerModule(module);

            return mapper.readValue(s, c);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * methods that can be overridden in the child class if needed
     */
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.registerModule(new JavaTimeModule());
        //mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "[]";
        }
    }

    /**
     * classes to convert between String and Message
     */
    public static class MessageToString extends DoFn<Message, String> {
        @ProcessElement
        public void processElement(ProcessContext processContext) throws Exception {
            Message element = processContext.element();
            processContext.output(element.toString());
        }
    }

    public static class MessageFromString extends DoFn<String, Message> {
        Message m;
        public MessageFromString(Class<? extends Message> c) {
            try {
                    // create an instance of type c using the nullary consturctor when options
                    // not available
                    m = c.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		@ProcessElement
        public void processElement(ProcessContext processContext) {
            try {
                Message x = m.fromString(processContext.element());
                processContext.output(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
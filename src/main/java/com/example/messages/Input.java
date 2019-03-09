package com.example.messages;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import org.apache.log4j.Logger;


/**
 *
 * */
public class Input extends Message {
    protected float value;
    protected Long timestamp;

    protected String valueKey = "/value";
    protected String timestampKey = "/timestamp";

    @JsonCreator
    public Input() {
    }

    public Input(String identifier, float value, Long timestamp) {
        super(identifier);
        this.value = value;
        this.timestamp = timestamp;
    }

    /**
     * supported transforms
     */

    public int compareTo(Message m) {
        Input t = (Input) m;
        return Float.compare(this.value, t.value);
    }

    public float getValue() {
        return value;
    };
    public void setValue(float value) {
        this.value = value;
    };

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * define CustomDeserializer to parse String into object of this class with customized schema
     */
    private class CustomDeserializer extends StdDeserializer<Input> {
        public final Logger logger = Logger.getLogger(CustomDeserializer.class);
        public CustomDeserializer() {
            this(null);
        }

        public CustomDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Input deserialize(JsonParser parser, DeserializationContext deserializer) {
            try {
                ObjectCodec codec = parser.getCodec();
                JsonNode node = codec.readTree(parser);
                String identifier = node.at(identifierKey).asText();
                float value = node.at(valueKey).floatValue();
                return new Input(
                    identifier,
                    value,
                    timestamp
                );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public Input fromString(String s) {
        return (Input)fromStringDefault(s, Input.class, new CustomDeserializer());
    }

    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Input t = (Input) o;
        // field comparison
        return (Objects.equals(value, t.value) && (Objects.equals(timestamp, t.timestamp)));
    }
}

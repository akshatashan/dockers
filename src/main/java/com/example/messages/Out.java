package com.example.messages;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;


/**
 *
 * */
public class Out extends Message{
    protected Long count;
    protected String window;
    //protected String valueKey = "/value";

    @JsonCreator
    public Out() {
    }

    public Out(String identifier, Long count, String window) {
        super(identifier);
        this.count = count;
        this.window = window;
    }

    public Long getCount() {
        return count;
    };
    public void setCount(Long count) {
        this.count = count;
    };

    public String getWindow() {
        return window;
    };
    public void setWindow(String window) {
        this.window = window;
    };

    // /**
    //  * define CustomDeserializer to parse String into object of this class with customized schema
    //  */
    // private class CustomDeserializer extends StdDeserializer<Out> {
    //     public CustomDeserializer() {
    //         this(null);
    //     }

    //     public CustomDeserializer(Class<?> vc) {
    //         super(vc);
    //     }

    //     @Override
    //     public Out deserialize(JsonParser parser, DeserializationContext deserializer) {
    //         try {
    //             ObjectCodec codec = parser.getCodec();
    //             JsonNode node = codec.readTree(parser);
    //             float value = node.at(valueKey).floatValue();

    //             return new Out(
    //                 identifier,
    //                 value,
    //                 ""
    //             );
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //             return null;
    //         }
    //     }
    // }

    // public Out fromString(String s) {
    //     return (Out)fromStringDefault(s, Out.class, new CustomDeserializer());
    // }

    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Out t = (Out) o;
        // field comparison
        return Objects.equals(count, t.count) && Objects.equals(window, t.window);
    }

    @Override
    public Message fromString(String s) {
        return null;
    }
}

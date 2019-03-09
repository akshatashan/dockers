package com.example.windows;

import org.apache.beam.sdk.transforms.windowing.AfterWatermark;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Repeatedly;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

/**
 * The basic window that fires when:
 *  1. the watermark passes the end of the window
 *  2. A late event arrives
 */
public class CustomWindow{
    /**
     * A basic fixed window
     * @param size               in seconds
     * @param allowedLateness    in seconds
     */
    public static <T> Window<T> fixedWindow(int size, int allowedLateness) {
        return Window.<T>into(FixedWindows.of(Duration.standardSeconds(size)))
            .triggering(
                Repeatedly.forever( // fire whenever late events arrive
                    AfterWatermark.pastEndOfWindow() // fire when the watermark passed the end of a window
                )
            )
            .withAllowedLateness(Duration.standardSeconds(allowedLateness)) // after the end of a window + duration (processing time), the window is no longer open.
            .accumulatingFiredPanes(); // accumulate elements within a window (when refiring for a window)
    }
}

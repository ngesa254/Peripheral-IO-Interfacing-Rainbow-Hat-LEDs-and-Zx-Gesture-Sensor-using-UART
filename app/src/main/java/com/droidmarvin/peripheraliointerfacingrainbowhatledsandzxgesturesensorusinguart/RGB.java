package com.droidmarvin.peripheraliointerfacingrainbowhatledsandzxgesturesensorusinguart;

import android.graphics.Color;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.SpiDevice;

import java.io.IOException;

/**
 * Created by Ngesa on 1/22/2018.
 */

public class RGB {
    private static final String APA102_RGB_7_LED_SLAVE = "SPI0.0";

    private static final byte LED_START_FRAME = (byte) 0b11100000;
    private static final byte LED_BRIGHTNESS = (byte) 0b00000011;
    private static final byte LED_BRIGHT_START_BYTE = (byte) (LED_START_FRAME | LED_BRIGHTNESS);
    private static final int ZERO_BITS = 0b0;
    private static final int TRANSACTION_SIZE = 37;

    private SpiDevice mRGB;
    private int position;

    /**
     * Call in on create to connect to the rainbow hat leds
     */
    public void setUpRGB() {

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            mRGB = service.openSpiDevice(APA102_RGB_7_LED_SLAVE);
        } catch (IOException e) {
            throw new IllegalStateException(APA102_RGB_7_LED_SLAVE + "error opening connection", e);
        }
        //RGB configuration
        try {
            mRGB.setMode(SpiDevice.MODE2);
//            bus.setFrequency(1_000_000); // 1Mhz
//            bus.setBitsPerWord(8);
//            bus.setBitJustification(true);
        } catch (IOException e) {
            throw new IllegalStateException(APA102_RGB_7_LED_SLAVE + " cannot be configured.", e);
        }
    }



    private static final class Color {
        private static final Color RED = new Color(50, 0, 0);
        private static final Color YELLOW = new Color(50, 50, 0);
        private static final Color PINK = new Color(50, 10, 12);
        private static final Color GREEN = new Color(0, 50, 0);
        private static final Color PURPLE = new Color(50, 0, 50);
        private static final Color ORANGE = new Color(50, 22, 0);
        private static final Color BLUE = new Color(0, 0, 50);

        static final Color[] RAINBOW = {
                Color.RED, Color.YELLOW, Color.PINK,
                Color.GREEN,
                Color.PURPLE, Color.ORANGE, Color.BLUE
        };

        int r;
        int g;
        int b;

        private Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

}

package com.droidmarvin.peripheraliointerfacingrainbowhatledsandzxgesturesensorusinguart;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.SpiDevice;

import java.io.IOException;

/**
 * Created by Ngesa on 1/22/2018.
 */

public class RGB {
    private static final String APA102_RGB_7_LED_SLAVE = "SPI0.0";

    private SpiDevice mRGB;

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

        try {
            mRGB.setMode(SpiDevice.MODE2);
//            bus.setFrequency(1_000_000); // 1Mhz
//            bus.setBitsPerWord(8);
//            bus.setBitJustification(true);
        } catch (IOException e) {
            throw new IllegalStateException(APA102_RGB_7_LED_SLAVE + " cannot be configured.", e);
        }
    }
}

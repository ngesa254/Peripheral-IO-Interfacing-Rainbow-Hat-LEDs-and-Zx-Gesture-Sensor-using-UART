package com.droidmarvin.peripheraliointerfacingrainbowhatledsandzxgesturesensorusinguart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.UartDevice;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String UART_GESTURE_SENSOR = "UART0";
    private UartDevice mZxGSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyZxGestureSensor();
    }

    private void setupZxGestureSensor() {
        try {
            PeripheralManagerService service = new PeripheralManagerService();
            mZxGSensor = service.openUartDevice(UART_GESTURE_SENSOR);

            mZxGSensor.setBaudrate(115200);
            mZxGSensor.setDataSize(8);
            mZxGSensor.setParity(UartDevice.PARITY_NONE);
            mZxGSensor.setStopBits(1);
        } catch (IOException e) {
            throw new IllegalStateException(UART_GESTURE_SENSOR + " error connecting to the pin/pin configuration", e);
        }
    }

    private void destroyZxGestureSensor() {
        if (mZxGSensor != null) {
            try {
                mZxGSensor.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing ZXGestureSensor", e);
            } finally {
                mZxGSensor = null;
            }
        }
    }
}


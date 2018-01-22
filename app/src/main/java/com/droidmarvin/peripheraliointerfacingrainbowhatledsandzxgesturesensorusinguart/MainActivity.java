package com.droidmarvin.peripheraliointerfacingrainbowhatledsandzxgesturesensorusinguart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String UART_GESTURE_SENSOR = "UART0";
    private static final byte MSG_CODE_GESTURE_EVENT = (byte) 0xFC;
    private static final byte GESTURE_CODE_SWIPE_RIGHT_EVENT = 0x01;
    private static final byte GESTURE_CODE_SWIPE_LEFT_EVENT = 0x02;
    private UartDevice mZxGSensor;

    private final RGB rgb = new RGB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rgb.setUpRGB();
        setupZxGestureSensor();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mZxGSensor.registerUartDeviceCallback(onUartBusHasData);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot listen for input from " + UART_GESTURE_SENSOR, e);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyZxGestureSensor();
        rgb.destroyRGB();
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


    private final UartDeviceCallback onUartBusHasData = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            try {
                byte[] buffer = new byte[3];
                while ((uart.read(buffer, buffer.length)) > 0) {
                    handleGestureSensorEvent(buffer);
                }
            } catch (IOException e) {
                Log.e("TUT", "Cannot read device data.", e);
            }

            return true;
        }

        private void handleGestureSensorEvent(byte[] payload) {
            byte messageCode = payload[0];
            if (messageCode != MSG_CODE_GESTURE_EVENT) {
                return;
            }
            byte gestureCode = payload[1];
            if (gestureCode == GESTURE_CODE_SWIPE_RIGHT_EVENT) {
                rgb.nextColor();
            } else if (gestureCode == GESTURE_CODE_SWIPE_LEFT_EVENT) {
                rgb.previousColor();
            }
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            Log.e("TUT", "ERROR " + error);
        }
    };


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


    @Override
    protected void onStop() {
        mZxGSensor.unregisterUartDeviceCallback(onUartBusHasData);
        super.onStop();
    }
}


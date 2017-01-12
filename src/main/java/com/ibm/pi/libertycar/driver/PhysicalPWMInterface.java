package com.ibm.pi.libertycar.driver;

import java.io.IOException;

import com.ibm.pi.libertycar.webapp.PWMInterfaceUnavailableException;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class PhysicalPWMInterface implements PWMInterface {
    private static final float PWM_DEVICE_FRQUENCY = 60;

    private I2CBus bus;
    private I2CDevice device;

    private int MODE1 = 0x00;
    private int PRESCALE = 0xFE;
    private int LED0_ON_L = 0x06;
    private int LED0_ON_H = 0x07;
    private int LED0_OFF_L = 0x08;
    private int LED0_OFF_H = 0x09;

    public PhysicalPWMInterface() throws PWMInterfaceUnavailableException {
        try {
            bus = I2CFactory.getInstance(1);
            device = bus.getDevice(0x40);
            device.write(MODE1, (byte) 0x00);
            setPWMFreq(PWM_DEVICE_FRQUENCY);
        } catch (UnsupportedBusNumberException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            throw new PWMInterfaceUnavailableException(t);
        }
    }

    private void setPWMFreq(float freq) throws IOException, InterruptedException {
        double freqScale = ((25000000 / 4096) / freq) - 1.0; // figure out freq scale
        freqScale = Math.floor(freqScale + 0.5);

        int oldMode = device.read(MODE1);
        int newMode = (oldMode & 0x7F) | 0x10;

        device.write(MODE1, (byte) newMode);
        Thread.sleep(20);
        device.write(PRESCALE, (byte) (Math.floor(freqScale)));
        Thread.sleep(20);
        device.write(MODE1, (byte) oldMode);
        Thread.sleep(60);
        device.write(MODE1, (byte) (oldMode | 0x80));
    }

    public void setPWM(int channel, int on, int off) throws IOException {
        device.write(LED0_ON_L + 4 * channel, (byte) (on & 0xFF));
        device.write(LED0_ON_H + 4 * channel, (byte) (on >> 8));
        device.write(LED0_OFF_L + 4 * channel, (byte) (off & 0xFF));
        device.write(LED0_OFF_H + 4 * channel, (byte) (off >> 8));
    }
}

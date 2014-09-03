package com.ibm.pi.libertycar.driver;
import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class CarDriver {
	private I2CBus bus;
	private I2CDevice device;

	private int MODE1       = 0x00;
	private int PRESCALE    = 0xFE;
	private int LED0_ON_L   = 0x06;
	private int LED0_ON_H   = 0x07;
	private int LED0_OFF_L   = 0x08;
	private int LED0_OFF_H  = 0x09;

	private boolean testMode = false;

	public CarDriver()  {
		try {
			if(!!!testMode){
				bus = I2CFactory.getInstance(1);
				device = bus.getDevice(0x40);
			} else {
				System.out.println("Car driver test mode enabled. Car hardware will not be accessed.");
			}
		} catch (Exception e) {
			System.out.println("No I2C bus found. Entering test mode.");
			testMode=true;
		}
	}

	public void setPWMFreq(float freq) throws IOException, InterruptedException{
		if(!!!testMode){
			double freqScale = ((25000000/4096)/freq)-1.0; //figure out freq scale
			freqScale = Math.floor(freqScale+0.5);

			int oldMode = device.read(MODE1);
			int newMode = (oldMode & 0x7F) | 0x10;

			device.write(MODE1, (byte) newMode);
//			Thread.sleep(20);
			device.write(PRESCALE, (byte)(Math.floor(freqScale)));
//			Thread.sleep(20);
			device.write(MODE1, (byte)oldMode);
			Thread.sleep(60);
			device.write(MODE1, (byte) (oldMode | 0x80));

		} else {
			System.out.println("Frequency would be set to "+freq);
		}
	}

	public void setPWM(int channel, int on, int off) throws IOException{
		if(!!!testMode){
			device.write(LED0_ON_L+4*channel, (byte) (on & 0xFF));
			device.write(LED0_ON_H+4*channel, (byte) (on >> 8));
			device.write(LED0_OFF_L+4*channel, (byte) (off & 0xFF));
			device.write(LED0_OFF_H+4*channel, (byte) (off >> 8));
//			System.out.println("Channel "+channel+ " IS set to "+on+" : "+off);

		} else {
//			System.out.println("Channel "+channel+ " set to "+on+" : "+off);
		}
	}
}

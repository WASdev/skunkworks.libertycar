package com.ibm.pi.libertycar.driver;

import java.io.IOException;

public class HardwareFactory {

	public static Hardware getHardware(boolean testMode) {
		if (!testMode) {
			try {
				return new I2CController();
			} catch (Throwable e) {
				System.out.println("No I2C bus found. Entering test mode.");
				e.printStackTrace();
			}
		}
		return new TestController();
	}

}

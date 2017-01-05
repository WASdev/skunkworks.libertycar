package com.ibm.pi.libertycar.driver;
import java.io.IOException;

public class PWMInterface {
	private final Hardware hardwareInterface;

	public PWMInterface()  {
		String testModeEnv = System.getenv("TEST_MODE");
		boolean testMode = Boolean.parseBoolean(testModeEnv);
		hardwareInterface = HardwareFactory.getHardware(testMode);

	}

	public void setPWM(int channel, int on, int off) throws IOException{
		hardwareInterface.setPWM(channel, on, off);
	}
}

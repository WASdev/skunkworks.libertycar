package com.ibm.pi.libertycar.driver;
import java.io.IOException;

public interface PWMInterface {
	public void setPWM(int channel, int on, int off) throws IOException ;
}

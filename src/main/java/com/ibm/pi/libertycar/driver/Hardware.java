package com.ibm.pi.libertycar.driver;

import java.io.IOException;

public interface Hardware {
	public void setPWM(int channel, int on, int off) throws IOException;
}

package com.ibm.pi.libertycar.driver;

import java.io.IOException;
import java.util.Date;

public class VirtualPWMInterface implements PWMInterface {
	
	private boolean first = true;
	private int lastChannel = -1;
	private int lastOn = -1;
	private int lastOff = -1;

	@Override
	public void setPWM(int channel, int on, int off) throws IOException {
		if (first || channel != lastChannel || on != lastOn || off != lastOff) {
			first = false;
			System.out.println(new Date() + ": Test: |"+channel+"|"+ on +"|"+ off+"|");
			lastChannel = channel;
			lastOn = on;
			lastOff = off;
		}
		
		
	}

}

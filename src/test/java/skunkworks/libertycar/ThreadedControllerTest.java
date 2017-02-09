package skunkworks.libertycar;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ibm.pi.libertycar.control.threaded.ThreadBasedCarController;
import com.ibm.pi.libertycar.driver.VirtualPWMInterface;

public class ThreadedControllerTest {
	
	@Test
	public void setAndGetSpeed() {
		ThreadBasedCarController tbcc = new ThreadBasedCarController(new VirtualPWMInterface());
		tbcc.setMaxSpeed(20);
		double returnedMaxSpeed = tbcc.getMaxSpeed();
		assertEquals("The set speed should be returned", 20, returnedMaxSpeed, 0.01);
	}
	
	@Test
	public void setOverMaxSpeed() {
		ThreadBasedCarController tbcc = new ThreadBasedCarController(new VirtualPWMInterface());
		tbcc.setMaxSpeed(201);
		double returnedMaxSpeed = tbcc.getMaxSpeed();
		assertEquals("The max allowed speed should be returned", 200, returnedMaxSpeed, 0.01);
	}
	
	@Test
	public void setUnderMinimumMaxSpeed() {
		ThreadBasedCarController tbcc = new ThreadBasedCarController(new VirtualPWMInterface());
		tbcc.setMaxSpeed(-1);
		double returnedMaxSpeed = tbcc.getMaxSpeed();
		assertEquals("The minimum setting for the max speed should be returned", 0, returnedMaxSpeed, 0.01);
	}

}

package skunkworks.libertycar;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.pi.libertycar.config.FrequencySettings;
import com.ibm.pi.libertycar.rest.config.ConfigApi;
import com.ibm.pi.libertycar.webapp.CarConfig;

import skunkworks.libertycar.util.TestController;
import skunkworks.libertycar.util.TestUtils;

public class ConfigApiTest {
	
	@Before
	public void tidyUp() {
		TestUtils.resetStaticClasses();
	}
	
	@Test
	@Ignore
	public void testFrequencySettings() {
		FrequencySettings newFrequencies = new FrequencySettings();
		newFrequencies.setLeftMax(1);
		newFrequencies.setMaxForward(2);
		newFrequencies.setMaxReverse(3);
		newFrequencies.setRightMax(4);
		newFrequencies.setSpeedNeutral(5);
		newFrequencies.setSteeringNeutral(6);
		
		TestController controller = new TestController();
		CarConfig.setControl(controller);
		
		ConfigApi configApi = new ConfigApi();
		configApi.setNewFrequencies(newFrequencies);
		
		double steeringIncrementSetting = controller.getSteeringIncrement();
		
		assertEquals("The steering increment should be updated", 1, steeringIncrementSetting, 0.01);
	}

}

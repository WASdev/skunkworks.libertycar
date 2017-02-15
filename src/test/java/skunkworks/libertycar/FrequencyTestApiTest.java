package skunkworks.libertycar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ibm.pi.libertycar.config.Globals;
import com.ibm.pi.libertycar.rest.config.FrequencyTestApi;

import skunkworks.libertycar.util.TestController;

public class FrequencyTestApiTest {
    
    @Test
    public void steeringTest() {
        TestController carController = new TestController();
        Globals.setController(carController);
        FrequencyTestApi frequencyTestApi = new FrequencyTestApi();
        frequencyTestApi.testSteering(4);
        
        assertEquals("The steering should have been set once", 4, carController.getLastSteeringTestFrequency());
        assertEquals("The steering should have been set once", 5, carController.getLastSteeringTestTime());
        assertEquals("The test should be called once only", 1, carController.getSteeringTestCount());
    }

    @Test
    public void speedTest() {
      
      TestController carController = new TestController();
      Globals.setController(carController);
      FrequencyTestApi frequencyTestApi = new FrequencyTestApi();
      frequencyTestApi.testSpeed(4);
      
      assertEquals("The speed test frequency should have been set", 4, carController.getLastSpeedTestFrequency());
      assertEquals("The speed test time should have been set", 5, carController.getLastSpeedTestTime());
      assertEquals("The test should be called once only", 1, carController.getSpeedTestCount());
   
    }

}

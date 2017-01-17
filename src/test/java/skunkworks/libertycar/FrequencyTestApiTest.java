package skunkworks.libertycar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Deque;

import org.junit.Test;

import com.ibm.pi.libertycar.config.Globals;
import com.ibm.pi.libertycar.control.CarController;
import com.ibm.pi.libertycar.control.FrequencyInstruction;
import com.ibm.pi.libertycar.rest.config.FrequencyTestApi;

import skunkworks.libertycar.util.CachingPWMInterface;

public class FrequencyTestApiTest {
    
    @Test
    public void steeringTest() {
        CachingPWMInterface cachingInterface = new CachingPWMInterface();
        CarController carController = new CarController(cachingInterface);
        Globals.setController(carController);
        FrequencyTestApi frequencyTestApi = new FrequencyTestApi();
        frequencyTestApi.testSteering(5);
        Deque<FrequencyInstruction> cache = cachingInterface.getInstructionCache();
        FrequencyInstruction lastSteeringInstruction = cache.remove();
        while (lastSteeringInstruction != null && lastSteeringInstruction.getChannel() != 14) {
            lastSteeringInstruction = cache.remove();
        }
        assertNotNull("There should be at least one instruction that affects the steering");
        assertEquals("The final instruction should have updated the steering", new FrequencyInstruction(14, 5, 5), lastSteeringInstruction);
    }

    @Test
    public void speedTest() {
        CachingPWMInterface cachingInterface = new CachingPWMInterface();
        CarController carController = new CarController(cachingInterface);
        Globals.setController(carController);
        FrequencyTestApi frequencyTestApi = new FrequencyTestApi();
        frequencyTestApi.testSpeed(5);
        Deque<FrequencyInstruction> cache = cachingInterface.getInstructionCache();
        FrequencyInstruction lastSpeedInstruction = cache.remove();
        while (lastSpeedInstruction != null && lastSpeedInstruction.getChannel() != 15) {
            lastSpeedInstruction = cache.remove();
        }
        assertNotNull("There should be at least one instruction that affects the steering");
        assertEquals("The final instruction should have updated the steering", new FrequencyInstruction(15, 5, 405), lastSpeedInstruction);
    }

}

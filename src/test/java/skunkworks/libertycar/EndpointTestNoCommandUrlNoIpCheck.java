package skunkworks.libertycar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.pi.libertycar.webapp.CarAdmin;

import skunkworks.libertycar.util.TestUtils;

public class EndpointTestNoCommandUrlNoIpCheck {
	
	@Before
	public void tidyUp() {
		TestUtils.resetStaticClasses();
		CarAdmin.lengthOfId = -1;
	}
	
	   
	@Test
	@Ignore
	public void userIsNull() {
        String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"\" }";
        
        TestUtils.sendRequestAndGetReqonse(messageToSend, "Generating ID. Please wait.");
	}
	
	@Test
	public void userInSteeringOnly() {
        CarAdmin.steeringControl.add("123");
        CarAdmin.controllerIds.add("123");
        
        String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"123\" }";

        TestUtils.sendRequestAndGetReqonse(messageToSend, "You are in full control - your ID is 123");
	}
	
	@Test
	public void userInThrottleOnly() {
        CarAdmin.throttleControl.add("123");
        CarAdmin.controllerIds.add("123");
        
        String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"123\" }";

        TestUtils.sendRequestAndGetReqonse(messageToSend, "You are in full control - your ID is 123");
	}
	
	@Test
	public void userInBothThrottleAndSteering() {
        CarAdmin.throttleControl.add("123");
        CarAdmin.steeringControl.add("123");
        CarAdmin.controllerIds.add("123");
        
        String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"123\" }";

        TestUtils.sendRequestAndGetReqonse(messageToSend, "You are in full control - your ID is 123");
	}
	
	@Test
	public void userRedirected() {
        CarAdmin.idsToForward.add("123");
        
        String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"123\" }";

        TestUtils.sendRequestAndGetReqonse(messageToSend, "You are in full control - your ID is 123");
	}

}

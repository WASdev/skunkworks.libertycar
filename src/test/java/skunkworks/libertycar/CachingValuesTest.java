package skunkworks.libertycar;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.ibm.pi.libertycar.control.CarControllerInterface;
import com.ibm.pi.libertycar.webapp.CarAdmin;
import com.ibm.pi.libertycar.webapp.CarControlEndpoint;

import skunkworks.libertycar.util.MockWebSocket;
import skunkworks.libertycar.util.TestController;
import skunkworks.libertycar.util.TestUtils;

public class CachingValuesTest {
	
	@Before
	public void tidyUp() {
		TestUtils.resetStaticClasses();
		CarAdmin.lengthOfId = -1;
	}
	
	   
	@Test
	public void sendSingleMessage() {
        String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"123\", msggrp: \"testGrp\" }";
        
        TestUtils.sendRequestAndGetReqonse(messageToSend, "You are in full control - your ID is 123");
	}
	
	@Test
	public void sendMessagesToSameGroup() {
        String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"123\", msggrp: \"testGrp\" }";
        
		CarControllerInterface carController = new TestController();
        CarControlEndpoint cce = new CarControlEndpoint();
        MockWebSocket session = new MockWebSocket();
        cce.onOpen(session, null);
        CarControlEndpoint.setControl(carController);
        cce.receiveMessage(messageToSend);
        String returnedString = session.getLastMessage();
        assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123", returnedString);
        
        cce.receiveMessage(messageToSend);
        returnedString = session.getLastMessage();
        assertEquals("The returned message should be what I expect", null, returnedString);
	}
	
	
	@Test
	public void sendMessagesToDifferentGroups() {
        String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"123\", msggrp: \"testGrp1\" }";
        
		CarControllerInterface carController = new TestController();
        CarControlEndpoint cce = new CarControlEndpoint();
        MockWebSocket session = new MockWebSocket();
        cce.onOpen(session, null);
        CarControlEndpoint.setControl(carController);
        cce.receiveMessage(messageToSend);
        String returnedString = session.getLastMessage();
        assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123", returnedString);
        
        String secondMessage = "{\"throttle\": 100, \"turning\": 100, \"id\": \"123\", msggrp: \"testGrp2\" }";
        
        cce.receiveMessage(secondMessage);
        returnedString = session.getLastMessage();
        assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123", returnedString);
	}

}

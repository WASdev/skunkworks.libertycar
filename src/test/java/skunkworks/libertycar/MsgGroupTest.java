package skunkworks.libertycar;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.ibm.pi.libertycar.webapp.CarAdmin;
import com.ibm.pi.libertycar.webapp.CarControlEndpoint;

import skunkworks.libertycar.util.CarValues;
import skunkworks.libertycar.util.MockWebSocket;
import skunkworks.libertycar.util.TestController;
import skunkworks.libertycar.util.TestUtils;

public class MsgGroupTest {

	@Before
	public void tidyUp() {
		TestUtils.resetStaticClasses();
		CarAdmin.lengthOfId = -1;
	}

	@Test
	public void noThrottleOnSecondRequest() {
		String messageToSend = "{\"throttle\": 10, \"turning\": 20, \"id\": \"123\"}";

		TestController carController = new TestController();
		CarControlEndpoint cce = new CarControlEndpoint();
		MockWebSocket session = new MockWebSocket();
		cce.onOpen(session, null);
		CarControlEndpoint.setControl(carController);
		cce.receiveMessage(messageToSend);
		String returnedString = session.getLastMessage();
		assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123",
				returnedString);
		CarValues firstValues = carController.getCurrentValues();

		assertEquals("The throttle should be set as per the request", 10, firstValues.getThrottle());
		assertEquals("The steering should be set as per the request", 20, firstValues.getSteering());

		String secondMessageToSend = "{\"turning\": 30, \"id\": \"123\"}";
		cce.receiveMessage(secondMessageToSend);
		returnedString = session.getLastMessage();
		assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123",
				returnedString);

		CarValues secondValues = carController.getCurrentValues();
		assertEquals("The throttle should be set as per the request", 10, secondValues.getThrottle());
		assertEquals("The steering should be set as per the request", 30, secondValues.getSteering());
	}

	@Test
	public void noSteeringOnSecondRequest() {
		String messageToSend = "{\"throttle\": 10, \"turning\": 20, \"id\": \"123\"}";

		TestController carController = new TestController();
		CarControlEndpoint cce = new CarControlEndpoint();
		MockWebSocket session = new MockWebSocket();
		cce.onOpen(session, null);
		CarControlEndpoint.setControl(carController);
		cce.receiveMessage(messageToSend);
		String returnedString = session.getLastMessage();
		assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123",
				returnedString);
		CarValues firstValues = carController.getCurrentValues();

		assertEquals("The throttle should be set as per the request", 10, firstValues.getThrottle());
		assertEquals("The steering should be set as per the request", 20, firstValues.getSteering());

		String secondMessageToSend = "{\"throttle\": 30, \"id\": \"123\"}";
		cce.receiveMessage(secondMessageToSend);
		returnedString = session.getLastMessage();
		assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123",
				returnedString);

		CarValues secondValues = carController.getCurrentValues();
		assertEquals("The throttle should be set as per the request", 30, secondValues.getThrottle());
		assertEquals("The steering should be set as per the request", 20, secondValues.getSteering());
	}

	@Test
	public void noValuesOnSecondRequest() {
		String messageToSend = "{\"throttle\": 10, \"turning\": 20, \"id\": \"123\"}";

		TestController carController = new TestController();
		CarControlEndpoint cce = new CarControlEndpoint();
		MockWebSocket session = new MockWebSocket();
		cce.onOpen(session, null);
		CarControlEndpoint.setControl(carController);
		cce.receiveMessage(messageToSend);
		String returnedString = session.getLastMessage();
		assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123",
				returnedString);
		CarValues firstValues = carController.getCurrentValues();

		assertEquals("The throttle should be set as per the request", 10, firstValues.getThrottle());
		assertEquals("The steering should be set as per the request", 20, firstValues.getSteering());

		String secondMessageToSend = "{\"id\": \"123\"}";
		cce.receiveMessage(secondMessageToSend);
		returnedString = session.getLastMessage();
		assertEquals("The returned message should be what I expect", "You are in full control - your ID is 123",
				returnedString);

		CarValues secondValues = carController.getCurrentValues();
		assertEquals("The throttle should be set as per the request", 10, secondValues.getThrottle());
		assertEquals("The steering should be set as per the request", 20, secondValues.getSteering());
	}

}

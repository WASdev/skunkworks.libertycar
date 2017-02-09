package skunkworks.libertycar.util;

import static org.junit.Assert.assertEquals;

import com.ibm.pi.libertycar.control.CarControllerInterface;
import com.ibm.pi.libertycar.webapp.CarAdmin;
import com.ibm.pi.libertycar.webapp.CarConfig;
import com.ibm.pi.libertycar.webapp.CarControlEndpoint;

public class TestUtils {

	public static void sendRequestAndGetReqonse(String messageToSend, String expectedResponse) {
		CarControllerInterface carController = new TestController();
        CarControlEndpoint cce = new CarControlEndpoint();
        MockWebSocket session = new MockWebSocket();
        cce.onOpen(session, null);
        CarControlEndpoint.setControl(carController);
        cce.receiveMessage(messageToSend);
        String returnedString = session.getLastMessage();
        assertEquals("The returned message should be what I expect", expectedResponse, returnedString);
	}

	public static void resetStaticClasses() {
		CarAdmin.lengthOfId = 3;
		CarConfig.commandURL = "";
		CarAdmin.idsToForward.clear();
        CarAdmin.controllerIds.clear();
        CarAdmin.steeringControl.clear();
        CarAdmin.throttleControl.clear();
		
	}
}

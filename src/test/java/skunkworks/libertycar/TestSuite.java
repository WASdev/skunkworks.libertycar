package skunkworks.libertycar;

import javax.websocket.Session;

import org.junit.Test;

import com.ibm.pi.libertycar.control.CarController;
import com.ibm.pi.libertycar.webapp.CarControlEndpoint;

import skunkworks.libertycar.util.MockWebSocket;

public class TestSuite {

	@Test
	public void test() {
		CarController carController = new CarController();
		CarControlEndpoint cce = new CarControlEndpoint();
		Session session = new MockWebSocket();
		cce.onOpen(session, null);
		cce.setControl(carController);
		cce.receiveMessage("{\"throttle\": 100, \"turning\": 100, \"id\": \"wibble\"}");
	}
}

package skunkworks.libertycar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ibm.pi.libertycar.control.CarControllerInterface;
import com.ibm.pi.libertycar.control.threaded.ThreadBasedCarController;
import com.ibm.pi.libertycar.driver.VirtualPWMInterface;
import com.ibm.pi.libertycar.webapp.CarControlEndpoint;

import skunkworks.libertycar.util.MockWebSocket;

public class TestSuite {

    @Test
    public void test() {
        CarControllerInterface carController = new ThreadBasedCarController(new VirtualPWMInterface());
        CarControlEndpoint cce = new CarControlEndpoint();
        MockWebSocket session = new MockWebSocket();
        cce.onOpen(session, null);
        CarControlEndpoint.setControl(carController);
        cce.receiveMessage("{\"throttle\": 100, \"turning\": 100, \"id\": \"wibble\"}");
        String returnedString = session.getLastMessage();
        assertEquals("The returned message should be what I expect", "You are in full control - your ID is wibble",
                returnedString);
    }

    @Test
    public void test2() {

    }
}

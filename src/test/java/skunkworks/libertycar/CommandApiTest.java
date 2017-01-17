package skunkworks.libertycar;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.taglibs.standard.tag.common.core.ForEachSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ibm.pi.libertycar.control.CarController;
import com.ibm.pi.libertycar.driver.VirtualPWMInterface;
import com.ibm.pi.libertycar.webapp.CarAdmin;
import com.ibm.pi.libertycar.webapp.CarConfig;
import com.ibm.pi.libertycar.webapp.CarControlEndpoint;

import skunkworks.libertycar.util.MockWebSocket;

@RunWith(Parameterized.class)
public class CommandApiTest {
    
    public static class CommandApiTestParameters {

        static final int IP_CHECK_THREE_CHAR = 3;
        static final int NO_IP_CHECK_MINUS_ONE_CHAR = -1;

        int ipCheckLength;
        String carId;
        String commandUrl;
        Collection<String> idsToAddToIpsToRemove;
        Collection<String> idsThatShouldBeredirected;
        Collection<String> idsThatShouldntBeRedirected;

        public CommandApiTestParameters(int ipCheckLength, String carId, String commandUrl, String[] idsToAddToIpsToRemove, String[] idsThatShouldBeredirected, String[] idsThatShouldntBeRedirected) {
            this.ipCheckLength = ipCheckLength;
            
            this.carId = carId;
            this.commandUrl =commandUrl;
            this.idsToAddToIpsToRemove = Arrays.asList(idsToAddToIpsToRemove);
            this.idsThatShouldBeredirected = Arrays.asList(idsThatShouldBeredirected);
            this.idsThatShouldntBeRedirected = Arrays.asList(idsThatShouldntBeRedirected);
        }

        @Override
        public String toString() {
            return "ipCheckLength=" + ipCheckLength + ",commandUrl="+commandUrl + ",carId="+carId+",idsToForward="+idsToAddToIpsToRemove+",idsToCheckForRedirect="+idsThatShouldBeredirected+",idsToCheckForNoRedirect="+idsThatShouldntBeRedirected;
        }

    }


    private CommandApiTestParameters testParameters;

    public CommandApiTest(CommandApiTestParameters parameters) {
        this.testParameters = parameters;
    }

    @Parameters(name = "{0}")
    public static Collection<CommandApiTestParameters> data() {
        Collection<CommandApiTestParameters> parameterList = new ArrayList<CommandApiTestParameters>();
        parameterList.add(new CommandApiTestParameters(CommandApiTestParameters.IP_CHECK_THREE_CHAR, "testCarId", "testUrl", new String[] {"123"}, new String[] {"123"}, new String[] {"456"}));
        parameterList.add(new CommandApiTestParameters(CommandApiTestParameters.NO_IP_CHECK_MINUS_ONE_CHAR, "testCarId", "testUrl", new String[] {"123"}, new String[] {}, new String[] {"123", "456"}));
        return parameterList;
    }
    
    @Test
    public void test() {
        CarController carController = new CarController(new VirtualPWMInterface());
        CarControlEndpoint cce = new CarControlEndpoint();
        
        // Set up a command URL and check that the ID is added
//        CommandAppApi commandApi = new CommandAppApi();
//        CommandAppSettings commandSettings = new CommandAppSettings();
//        commandSettings.setCommandURL("testUrl");
//        commandSettings.setCarID("testCarId");
//        commandApi.setCommandAppSettings(commandSettings);

        CarAdmin.lengthOfId = testParameters.ipCheckLength;
        CarConfig.carID = testParameters.carId;
        CarConfig.commandURL = testParameters.commandUrl;
        CarAdmin.idsToForward.addAll(testParameters.idsToAddToIpsToRemove);
        
        MockWebSocket session = new MockWebSocket();
        cce.onOpen(session, null);
        CarControlEndpoint.setControl(carController);
        
        for(String idThatShouldBeForwarded : testParameters.idsThatShouldBeredirected) {
            String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"" + idThatShouldBeForwarded + "\"}";
            cce.receiveMessage(messageToSend);
            String returnedString = session.getLastMessage();
            assertEquals("The returned message should be what I expect", "testUrl/leaderboard",
                    returnedString);

            cce.receiveMessage(messageToSend);
            String expectedMessage = "You do not have control. Your ID is " + idThatShouldBeForwarded;
            returnedString = session.getLastMessage();
            assertEquals("After the first run the ID is removed from the list, so the returned message the secodn time should show the car not under the user's control", expectedMessage,
                    returnedString);

        }
        
        
        for (String idThatShouldNotBeRedirected: testParameters.idsThatShouldntBeRedirected) {
            String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"" + idThatShouldNotBeRedirected + "\"}";
            cce.receiveMessage(messageToSend);
            String returnedString = session.getLastMessage();
            String expectedMessage = "You do not have control. Your ID is " + idThatShouldNotBeRedirected;
            if (testParameters.ipCheckLength <= 0) {
                expectedMessage = "You are in full control - your ID is " + idThatShouldNotBeRedirected;
            }
            
            assertEquals("After the first run the ID is removed from the list, so the returned message the secodn time should show the car not under the user's control", expectedMessage,
                    returnedString);
        }
    }


}

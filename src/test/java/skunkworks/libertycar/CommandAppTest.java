package skunkworks.libertycar;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ibm.pi.libertycar.control.CarControllerInterface;
import com.ibm.pi.libertycar.control.threaded.ThreadBasedCarController;
import com.ibm.pi.libertycar.driver.VirtualPWMInterface;
import com.ibm.pi.libertycar.webapp.CarAdmin;
import com.ibm.pi.libertycar.webapp.CarConfig;
import com.ibm.pi.libertycar.webapp.CarControlEndpoint;

import skunkworks.libertycar.util.MockWebSocket;

@RunWith(Parameterized.class)
public class CommandAppTest {

    public static class Outcome {

        @Override
        public String toString() {
            return "RedirectedIds=" + idsThatShouldBeredirected + ", NonRedirectedIds=" + idsThatShouldntBeRedirected;
        }

        Collection<String> idsThatShouldBeredirected;
        Collection<String> idsThatShouldntBeRedirected;

        public Outcome(String[] idsThatShouldBeredirected, String[] idsThatShouldntBeRedirected) {
            this.idsThatShouldBeredirected = Arrays.asList(idsThatShouldBeredirected);
            this.idsThatShouldntBeRedirected = Arrays.asList(idsThatShouldntBeRedirected);
        }

    }

    public static class Scenario {

        static final int IP_CHECK_THREE_CHAR = 3;
        static final int NO_IP_CHECK_MINUS_ONE_CHAR = -1;

        @Override
        public String toString() {
            return "Given CheckLength=" + ipCheckLength + ", carId=" + carId + ", cmmndUrl=" + commandUrl
                    + ", Ips To Remove=" + idsToAddToIpsToRemove + "]";
        }

        int ipCheckLength;
        String carId;
        String commandUrl;
        Collection<String> idsToAddToIpsToRemove;

        public Scenario(int ipCheckLength, String carId, String commandUrl, String[] idsToAddToIpsToRemove) {
            this.ipCheckLength = ipCheckLength;

            this.carId = carId;
            this.commandUrl = commandUrl;
            this.idsToAddToIpsToRemove = Arrays.asList(idsToAddToIpsToRemove);
        }

    }

    public static class TestParms {

        Scenario scenario;
        Outcome outcome;

        public TestParms(Scenario scenario, Outcome outcome) {

            this.scenario = scenario;
            this.outcome = outcome;

        }

        @Override
        public String toString() {
            return scenario + " causes " + outcome;
        }

    }

    private TestParms testParms;

    public CommandAppTest(TestParms parameters) {
        this.testParms = parameters;
    }

    @Before
    public void removeSwitches() {
        Collection<String> idsToRemove = new ArrayList<String>(CarAdmin.idsToForward);
        CarAdmin.idsToForward.removeAll(idsToRemove);
    }

    @Parameters(name = "{0}")
    public static Collection<TestParms> data() {
        Collection<TestParms> parameterList = new ArrayList<TestParms>();

        Outcome oneIsForwarded = new Outcome(new String[] { "123" }, new String[] { "456" });
        Outcome neitherAreForwarded = new Outcome(new String[] {}, new String[] { "123", "456" });

        parameterList.add(new TestParms(
                new Scenario(Scenario.IP_CHECK_THREE_CHAR, "testCarId", "testUrl", new String[] { "123" }),
                oneIsForwarded));
        parameterList
                .add(new TestParms(new Scenario(Scenario.IP_CHECK_THREE_CHAR, "testCarId", "testUrl", new String[] {}),
                        neitherAreForwarded));
        parameterList
                .add(new TestParms(new Scenario(Scenario.IP_CHECK_THREE_CHAR, "testCarId", "", new String[] { "123" }),
                        neitherAreForwarded));
        parameterList.add(new TestParms(new Scenario(Scenario.IP_CHECK_THREE_CHAR, "testCarId", "", new String[] {}),
                neitherAreForwarded));

        parameterList.add(new TestParms(
                new Scenario(Scenario.IP_CHECK_THREE_CHAR, "", "testUrl", new String[] { "123" }), oneIsForwarded));
        parameterList.add(new TestParms(new Scenario(Scenario.IP_CHECK_THREE_CHAR, "", "testUrl", new String[] {}),
                neitherAreForwarded));
        parameterList.add(new TestParms(new Scenario(Scenario.IP_CHECK_THREE_CHAR, "", "", new String[] { "123" }),
                neitherAreForwarded));
        parameterList.add(new TestParms(new Scenario(Scenario.IP_CHECK_THREE_CHAR, "", "", new String[] {}),
                neitherAreForwarded));

        parameterList.add(new TestParms(
                new Scenario(Scenario.NO_IP_CHECK_MINUS_ONE_CHAR, "testCarId", "testUrl", new String[] { "123" }),
                neitherAreForwarded));
        parameterList.add(new TestParms(
                new Scenario(Scenario.NO_IP_CHECK_MINUS_ONE_CHAR, "testCarId", "testUrl", new String[] {}),
                neitherAreForwarded));
        parameterList.add(new TestParms(
                new Scenario(Scenario.NO_IP_CHECK_MINUS_ONE_CHAR, "testCarId", "", new String[] { "123" }),
                neitherAreForwarded));
        parameterList
                .add(new TestParms(new Scenario(Scenario.NO_IP_CHECK_MINUS_ONE_CHAR, "testCarId", "", new String[] {}),
                        neitherAreForwarded));

        parameterList.add(
                new TestParms(new Scenario(Scenario.NO_IP_CHECK_MINUS_ONE_CHAR, "", "testUrl", new String[] { "123" }),
                        neitherAreForwarded));
        parameterList
                .add(new TestParms(new Scenario(Scenario.NO_IP_CHECK_MINUS_ONE_CHAR, "", "testUrl", new String[] {}),
                        neitherAreForwarded));
        parameterList
                .add(new TestParms(new Scenario(Scenario.NO_IP_CHECK_MINUS_ONE_CHAR, "", "", new String[] { "123" }),
                        neitherAreForwarded));
        parameterList.add(new TestParms(new Scenario(Scenario.NO_IP_CHECK_MINUS_ONE_CHAR, "", "", new String[] {}),
                neitherAreForwarded));

        // Haven't tested when an ID has control

        return parameterList;
    }

    @Test
    public void test() {
        CarControllerInterface carController = new ThreadBasedCarController(new VirtualPWMInterface());
        CarControlEndpoint cce = new CarControlEndpoint();

        // Set up a command URL and check that the ID is added
        // CommandAppApi commandApi = new CommandAppApi();
        // CommandAppSettings commandSettings = new CommandAppSettings();
        // commandSettings.setCommandURL("testUrl");
        // commandSettings.setCarID("testCarId");
        // commandApi.setCommandAppSettings(commandSettings);

        CarAdmin.lengthOfId = testParms.scenario.ipCheckLength;
        CarConfig.carID = testParms.scenario.carId;
        CarConfig.commandURL = testParms.scenario.commandUrl;
        // TODO this needs clearing each time as it is a static
        CarAdmin.idsToForward.addAll(testParms.scenario.idsToAddToIpsToRemove);

        MockWebSocket session = new MockWebSocket();
        cce.onOpen(session, null);
        CarControlEndpoint.setControl(carController);

        for (String idThatShouldBeForwarded : testParms.outcome.idsThatShouldBeredirected) {
            String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"" + idThatShouldBeForwarded + "\"}";
            cce.receiveMessage(messageToSend);
            String returnedString = session.getLastMessage();
            assertEquals("The returned message should be what I expect", "testUrl/leaderboard", returnedString);

            cce.receiveMessage(messageToSend);
            String expectedMessage = "You do not have control. Your ID is " + idThatShouldBeForwarded;
            returnedString = session.getLastMessage();
            assertEquals(
                    "After the first run the ID is removed from the list, so the returned message the secodn time should show the car not under the user's control",
                    expectedMessage, returnedString);

        }

        for (String idThatShouldNotBeRedirected : testParms.outcome.idsThatShouldntBeRedirected) {
            String messageToSend = "{\"throttle\": 100, \"turning\": 100, \"id\": \"" + idThatShouldNotBeRedirected
                    + "\"}";
            cce.receiveMessage(messageToSend);
            String returnedString = session.getLastMessage();
            String expectedMessage = "You do not have control. Your ID is " + idThatShouldNotBeRedirected;
            if (testParms.scenario.ipCheckLength <= 0) {
                expectedMessage = "You are in full control - your ID is " + idThatShouldNotBeRedirected;
            }

            assertEquals(
                    "After the first run the ID is removed from the list, so the returned message the secodn time should show the car not under the user's control",
                    expectedMessage, returnedString);
        }
    }

}

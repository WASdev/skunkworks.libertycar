package skunkworks.libertycar;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ibm.pi.libertycar.config.EndpointConfig;
import com.ibm.pi.libertycar.rest.config.EndpointConfigApi;

public class EndpointConfigApiTest {

  @Test
  public void test() {
    EndpointConfigApi eca = new EndpointConfigApi();
    EndpointConfig endpointConfig = eca.getConfig(null);
    assertNotNull("There should be an endpoint object returned", endpointConfig);
    assertNotNull("There should be a websocket Url", endpointConfig.getWebSocketUrl());
  }
}

package skunkworks.libertycar;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.ibm.pi.libertycar.config.EndpointConfig;
import com.ibm.pi.libertycar.rest.config.EndpointConfigApi;

import skunkworks.libertycar.util.MockRequest;

public class EndpointConfigApiTest {

  @Test
  public void testNonValidUser() throws IOException {
    EndpointConfigApi eca = new EndpointConfigApi();
    MockRequest request = new MockRequest("http://testHost:333/SOmething/somehwere");
    EndpointConfig endpointConfig = eca.getConfig(request);
    assertNotNull("There should be an endpoint object returned", endpointConfig);
    assertNotNull("There should be a websocket Url", endpointConfig.getWebSocketUrl());
  }
}

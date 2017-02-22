package com.ibm.pi.libertycar.rest.config;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ibm.pi.libertycar.config.EndpointConfig;

@Path("/endpointconfig")
public class EndpointConfigApi {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public EndpointConfig getConfig(@Context HttpServletRequest request) {
    System.out.println("Test");
    
    String webSocketUrl = System.getenv("WEBSOCKET_URL");
    if (webSocketUrl == null) {
      URI url = URI.create(request.getRequestURL().toString());
      webSocketUrl = "ws://" + url.getHost() + ":" + url.getPort() + "/LibertyCar/control";
    }
    System.out.println("returning " + webSocketUrl);
    return new EndpointConfig(webSocketUrl);
  }

}

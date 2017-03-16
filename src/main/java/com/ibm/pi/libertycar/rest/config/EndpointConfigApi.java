package com.ibm.pi.libertycar.rest.config;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ibm.pi.libertycar.config.EndpointConfig;
import com.ibm.pi.libertycar.config.Globals;
import com.ibm.pi.libertycar.security.UserId;

@Path("/endpointconfig")
public class EndpointConfigApi {


  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public EndpointConfig getConfig(@Context HttpServletRequest request) {
    System.out.println("Test");
    
    String webSocketUrl = System.getenv("WEBSOCKET_URL");
    String userRestUrl = System.getenv("USER_REST_URL");
    if (webSocketUrl == null) {
      URI url = URI.create(request.getRequestURL().toString());
      webSocketUrl = "ws://" + url.getHost() + ":" + url.getPort() + "/LibertyCar/control";
    }
    if (userRestUrl == null) {
      URI url = URI.create(request.getRequestURL().toString());
      userRestUrl = url.getScheme() + "://" + url.getHost() + ":" + url.getPort() + "/LibertyCar/api/endpointconfig";
    }
    System.out.println("returning " + webSocketUrl);
    return new EndpointConfig(webSocketUrl, userRestUrl);
  }
  
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public UserId validateUser(UserId user) {
    if (Globals.getUserIdManager().isUserValid(user)) return user;
    return Globals.getUserIdManager().getNewUser();
  }

}

package com.ibm.pi.libertycar.config;

public class EndpointConfig {

  private final String webSocketUrl;

  public EndpointConfig(String webSocketUrl) {
    this.webSocketUrl = webSocketUrl;
  }
  
  public String getWebSocketUrl() {
    return webSocketUrl;
  }

}

package com.ibm.pi.libertycar.config;

public class EndpointConfig {

  private final String webSocketUrl;
  private final String userRestUrl;
  
  public EndpointConfig(String webSocketUrl, String userRestUrl) {
    this.webSocketUrl = webSocketUrl;
    this.userRestUrl = userRestUrl;
  }
  
  public String getWebSocketUrl() {
    return webSocketUrl;
  }

  public String getUserRestUrl() {
    return userRestUrl;
  }
}

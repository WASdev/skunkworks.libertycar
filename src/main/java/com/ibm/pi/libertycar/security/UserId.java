package com.ibm.pi.libertycar.security;

public class UserId {

  @Override
  public String toString() {
    return "UserId [userId=" + userId + ", password=" + password + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((userId == null) ? 0 : userId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserId other = (UserId) obj;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    if (userId == null) {
      if (other.userId != null)
        return false;
    } else if (!userId.equals(other.userId))
      return false;
    return true;
  }

  private String userId;
  private String password;
  
  public UserId(String authToken) {
    setAuth(authToken);
  }
  
  public UserId() {}
  
  public String getUserName() {
    return userId;
  }

  public String getPassword() {
    return password;
  }
  
  public void setAuth(String authToken) {
    String[] parts = authToken.split(":", 2);
    this.userId = parts[0];
    if (parts.length > 1) {
      this.password = parts[1];
    }
    
  }

}

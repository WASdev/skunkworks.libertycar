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

  private final String userId;
  private final String password;
  
  public UserId(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }
  
  public String getUserName() {
    return userId;
  }

  public String getPassword() {
    return password;
  }

}

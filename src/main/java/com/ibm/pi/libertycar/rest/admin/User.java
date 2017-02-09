package com.ibm.pi.libertycar.rest.admin;

public class User {

  private String userId;
  private boolean steeringControl;
  private boolean throttleControl;

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setSteeringControl(boolean steeringControl) {
    this.steeringControl = steeringControl;
  }

  public void setThrottleControl(boolean throttleControl) {
    this.throttleControl = throttleControl;
  }

  public String getUserId() {
    return userId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (steeringControl ? 1231 : 1237);
    result = prime * result + (throttleControl ? 1231 : 1237);
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
    User other = (User) obj;
    if (steeringControl != other.steeringControl)
      return false;
    if (throttleControl != other.throttleControl)
      return false;
    if (userId == null) {
      if (other.userId != null)
        return false;
    } else if (!userId.equals(other.userId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "User [userId=" + userId + ", steeringControl=" + steeringControl + ", throttleControl=" + throttleControl
        + "]";
  }

  public boolean getSteeringControl() {
    return steeringControl;
  }

  public boolean getThrottleControl() {
    return throttleControl;
  }

  public static User add(String userId, boolean steeringControl, boolean throttleControl) {
    User newUser = new User();
    newUser.setUserId(userId);
    newUser.setSteeringControl(steeringControl);
    newUser.setThrottleControl(throttleControl);
    return newUser;
  }

  
}

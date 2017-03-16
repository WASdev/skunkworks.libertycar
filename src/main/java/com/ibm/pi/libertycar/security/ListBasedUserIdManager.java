package com.ibm.pi.libertycar.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ListBasedUserIdManager implements UserIdManager {
  
  ConcurrentLinkedDeque<String> availableUserNames = new ConcurrentLinkedDeque<String>();
  
  public ListBasedUserIdManager() throws IOException {
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/users.list");
    if (in == null) {
      in = this.getClass().getResourceAsStream("/users.list");
    }
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String line = null;
    Set<String> users = new TreeSet<String>();
    while ((line = br.readLine()) != null) {
      if (line.trim().length() > 0) { 
        users.add(line.trim());
      }
    }
    availableUserNames.addAll(users);
    System.out.println("List of users: " + availableUserNames);
  }

  ConcurrentHashMap<String, UserId> userList = new ConcurrentHashMap<String, UserId>();
  /* (non-Javadoc)
   * @see com.ibm.pi.libertycar.security.UserIdManager#getNewUser()
   */
  @Override
  public UserId getNewUser() {
    String userName = availableUserNames.pollFirst();
    UserId newUser = null;
    if (userName != null) {
      newUser =  new UserId(userName+ ":"+ UUID.randomUUID().toString());
      userList.putIfAbsent(newUser.getUserName(), newUser);
    }
    System.out.println("Creating new user " + newUser);
    return newUser;
  }

  /* (non-Javadoc)
   * @see com.ibm.pi.libertycar.security.UserIdManager#isUserValid(com.ibm.pi.libertycar.security.UserId)
   */
  @Override
  public boolean isUserValid(UserId user) {
    if (user == null || !userList.containsKey(user.getUserName())) {
      return false;
    }
    
    UserId storedUser = userList.get(user.getUserName());
    
    return storedUser.equals(user);
  }

  /* (non-Javadoc)
   * @see com.ibm.pi.libertycar.security.UserIdManager#revokeUser(com.ibm.pi.libertycar.security.UserId)
   */
  @Override
  public void revokeUser(UserId user) {
    userList.remove(user.getUserName(), user);
    availableUserNames.addLast(user.getUserName());
  }

}

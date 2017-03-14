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

public class UserIdManager {
  
  ConcurrentLinkedDeque<String> availableUserNames = new ConcurrentLinkedDeque<String>();
  
  public UserIdManager() throws IOException {
    InputStream in = UserIdManager.class.getClass().getResourceAsStream("/users.list");
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
  public UserId getNewUser() {
    String userName = availableUserNames.pollFirst();
    UserId newUser = null;
    if (userName != null) {
      newUser =  new UserId(availableUserNames.pollFirst(), UUID.randomUUID().toString());
      userList.putIfAbsent(newUser.getUserName(), newUser);
    }
    System.out.println("Creating new user " + newUser);
    return newUser;
  }

  public boolean isUserValid(UserId user) {
    if (user == null || !userList.containsKey(user.getUserName())) {
      return false;
    }
    
    UserId storedUser = userList.get(user.getUserName());
    
    return storedUser.equals(user);
  }

  public void revokeUser(UserId user) {
    userList.remove(user.getUserName(), user);
    availableUserNames.addLast(user.getUserName());
  }

}

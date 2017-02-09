package com.ibm.pi.libertycar.rest.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ibm.pi.libertycar.config.Globals;
import com.ibm.pi.libertycar.webapp.CarAdmin;

@Path("/admin")
public class AdminApi {
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AdminConfigSettings getAdminConfig() {
        return new AdminConfigSettings(Globals.getController().getMaxSpeed(), CarAdmin.lengthOfId);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public User addNewUser(User user) {
      synchronized (CarAdmin.controllerIds) {
        if (!CarAdmin.controllerIds.contains(user.getUserId())) {
          CarAdmin.controllerIds.add(user.getUserId());
        }
        
        if (user.getSteeringControl()) {
          CarAdmin.steeringControl.add(user.getUserId());
        } else if (CarAdmin.steeringControl.contains(user.getUserId())) {
          CarAdmin.steeringControl.remove(user.getUserId());
        }
        
        if (user.getThrottleControl()) {
          CarAdmin.throttleControl.add(user.getUserId());
        } else if (CarAdmin.throttleControl.contains(user.getUserId())) {
          CarAdmin.throttleControl.remove(user.getUserId());
        }
      }
      System.out.println("Updated user " + user);
      return user;
    }

    @Path("/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
      List<User> users = new ArrayList<User>();
      synchronized (CarAdmin.controllerIds) {
        for (String userId : CarAdmin.controllerIds) {
          boolean steeringControl = (CarAdmin.steeringControl.contains(userId)) ? true : false;
          boolean throttleControl = (CarAdmin.throttleControl.contains(userId)) ? true : false;
          users.add(User.add(userId, steeringControl, throttleControl));
        }
      }
      System.out.println("Returning " + users);
      return users;
    }

    @Path("/users/{userid}")
    @DELETE
    public void removeUser(@PathParam("userid") String userId) {
      synchronized (CarAdmin.controllerIds) {
        CarAdmin.controllerIds.remove(userId);
        CarAdmin.steeringControl.remove(userId);
        CarAdmin.throttleControl.remove(userId);
      }
    }

}

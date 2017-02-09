package skunkworks.libertycar;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.pi.libertycar.config.Globals;
import com.ibm.pi.libertycar.control.threaded.ThreadBasedCarController;
import com.ibm.pi.libertycar.driver.VirtualPWMInterface;
import com.ibm.pi.libertycar.rest.admin.AdminApi;
import com.ibm.pi.libertycar.rest.admin.AdminConfigSettings;
import com.ibm.pi.libertycar.rest.admin.User;
import com.ibm.pi.libertycar.webapp.CarAdmin;

public class AdminApiTest {
  
  AdminApi api;
  
  @Before
  public void setup() {
    CarAdmin.lengthOfId = -1;
    api = new AdminApi();
    Globals.setController(new ThreadBasedCarController(new VirtualPWMInterface()));
  }
  
  @After
  public void tidyUp() {
    Globals.setController(null);
    CarAdmin.controllerIds.clear();
    CarAdmin.steeringControl.clear();
    CarAdmin.throttleControl.clear();
  }

  @Test
  public void getInitialSettings() {
    AdminConfigSettings adminConfigSettings = api.getAdminConfig();
    assertEquals("The max speed should be the default", 50, adminConfigSettings.getCarMaxSpeed(), 0.01);
    assertEquals("The IP check length should be the default", -1, adminConfigSettings.getUserIdLengthCheck());
  }

  public User addUser(User newUser) {
    List<User> initialList = api.getUsers();
    assertEquals("The list should be empty", 0, initialList.size());

    User returnedUser = api.addNewUser(newUser);
    assertEquals("The new user should match the returned user", newUser, returnedUser);
    List<User> secondList = api.getUsers();
    assertEquals("The list should contain the new user", 1, secondList.size());
    User foundUser = secondList.get(0);
    assertEquals("The added user should be the user we added", newUser, foundUser);

    return foundUser;
  }

  private void updateUser(User updatedUser) {
    List<User> initialList = api.getUsers();
    assertEquals("The list should be empty", 1, initialList.size());

    User returnedUser = api.addNewUser(updatedUser);
    assertEquals("The new user should match the returned user", updatedUser, returnedUser);
    List<User> secondList = api.getUsers();
    assertEquals("The list should contain the new user", 1, secondList.size());
    User foundUser = secondList.get(0);
    assertEquals("The added user should be the user we added", updatedUser, foundUser);
  }
  @Test
  public void addUserWithNoControl() {
    User newUser = User.add("123", false, false);
    addUser(newUser);
  }

  @Test
  public void addUserWithSteeringControl() {
    User newUser = User.add("123", true, false);
    addUser(newUser);
  }

  @Test
  public void addUserWithThrottleControl() {
    User newUser = User.add("123", false, true);
    addUser(newUser);
  }
  
  @Test
  public void addUserWithFullControl() {
    User newUser = User.add("123", true, true);
    addUser(newUser);
  }
  
  
  @Test
  public void addSteeringToUser() {
    User newUser = User.add("123", false, false);
    addUser(newUser);
    
    User updatedUser = User.add("123", true, false);
    updateUser(updatedUser);
  }

  @Test
  public void removeSteeringFromUser() {
    User newUser = User.add("123", true, false);
    addUser(newUser);
    
    User updatedUser = User.add("123", false, false);
    updateUser(updatedUser);
  }
  @Test
  public void addThrottleToUser() {
    User newUser = User.add("123", false, false);
    addUser(newUser);
    
    User updatedUser = User.add("123", false, true);
    updateUser(updatedUser);
  }

  @Test
  public void removeThrottleFromUser() {
    User newUser = User.add("123", false, true);
    addUser(newUser);
    
    User updatedUser = User.add("123", false, false);
    updateUser(updatedUser);
  }
  
  @Test
  public void addUserThenRemvoeThem() {
    User newUser = User.add("123", false, false);
    addUser(newUser);
    api.removeUser("123");
    List<User> userList = api.getUsers();
    assertEquals("There should be no users left", 0, userList.size());
  }
}

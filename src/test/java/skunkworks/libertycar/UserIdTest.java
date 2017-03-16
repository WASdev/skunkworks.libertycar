package skunkworks.libertycar;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;

import com.ibm.pi.libertycar.security.UserId;
import com.ibm.pi.libertycar.security.UserIdManager;
import com.ibm.pi.libertycar.security.ListBasedUserIdManager;

public class UserIdTest {

  @Test
  public void getSingleUser() throws IOException {
    UserIdManager uim = new ListBasedUserIdManager();
    
    UserId user = uim.getNewUser();
    
    assertNotNull("We should be able to generate a new user", user);
    assertNotNull("The user should have a user ID", user.getUserName());
    assertNotNull("The user should have a randomly generated password", user.getPassword());

    assertTrue("A newly created user should be valid", uim.isUserValid(user));

    uim.revokeUser(user);
    assertFalse("A newly created user should be valid", uim.isUserValid(user));
  }
  
  @Test
  public void nullUserIsntValid() throws IOException {
    UserIdManager uim = new ListBasedUserIdManager();
    assertFalse("Null should not be accepted as a userId", uim.isUserValid(null));
  }
  
  @Test
  public void unknownUserIsntValid() throws IOException {
    UserIdManager uim = new ListBasedUserIdManager();
    UserId newUser =  new UserId(UUID.randomUUID().toString() + ":"+ UUID.randomUUID().toString());
    assertFalse("An unknown user should not be valid", uim.isUserValid(newUser));
  }
  
  @Test
  public void differentObjectsWithSameValuesAreBothValid() throws IOException {
    UserIdManager uim = new ListBasedUserIdManager();
    
    UserId user = uim.getNewUser();
    
    assertNotNull("We should be able to generate a new user", user);
    assertNotNull("The user should have a user ID", user.getUserName());
    assertNotNull("The user should have a randomly generated password", user.getPassword());

    assertTrue("A newly created user should be valid", uim.isUserValid(user));
    
    UserId clonedUser = new UserId(user.getUserName()+ ":"+ user.getPassword());
    
    assertTrue("The clone should be valid if the original is valid", uim.isUserValid(clonedUser));
    
    uim.revokeUser(clonedUser);
    
    assertFalse("If the cloen has revoked the validity, then the original should not be valid", uim.isUserValid(user));
    
  }
  
  @Test
  public void validUserInvalidPassword() throws IOException {
    UserIdManager uim = new ListBasedUserIdManager();
    
    UserId user = uim.getNewUser();
    
    assertNotNull("We should be able to generate a new user", user);
    assertNotNull("The user should have a user ID", user.getUserName());
    assertNotNull("The user should have a randomly generated password", user.getPassword());

    assertTrue("A newly created user should be valid", uim.isUserValid(user));
    
    UserId clonedUser = new UserId(user.getUserName()+ ":"+ "Unlikely to be a UUID");
    
    assertFalse("The clone should not be valid becuase the password is different", uim.isUserValid(clonedUser));
    
    uim.revokeUser(clonedUser);
    
    assertTrue("The original should be valid becuase the clone should not be able to revoke it", uim.isUserValid(user));
    
  }
  
  @Test
  public void createTwoUsers() throws IOException {
    UserIdManager uim = new ListBasedUserIdManager();
    
    UserId user1 = uim.getNewUser();
    
    assertNotNull("We should be able to generate a new user", user1);
    assertNotNull("The user should have a user ID", user1.getUserName());
    assertNotNull("The user should have a randomly generated password", user1.getPassword());

    assertTrue("A newly created user should be valid", uim.isUserValid(user1));

    UserId user2 = uim.getNewUser();
    
    assertNotNull("We should be able to generate a new user", user2);
    assertNotNull("The user should have a user ID", user2.getUserName());
    assertNotNull("The user should have a randomly generated password", user2.getPassword());

    assertTrue("A newly created user should be valid", uim.isUserValid(user2));

    assertNotEquals("The two users should not be the same:", user1, user2);
  }
}

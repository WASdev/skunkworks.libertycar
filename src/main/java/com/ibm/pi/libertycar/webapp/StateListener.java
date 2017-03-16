package com.ibm.pi.libertycar.webapp;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ibm.pi.libertycar.config.Globals;
import com.ibm.pi.libertycar.control.CarControllerInterface;
import com.ibm.pi.libertycar.control.threaded.ThreadBasedCarController;
import com.ibm.pi.libertycar.driver.PWMInterface;
import com.ibm.pi.libertycar.driver.PhysicalPWMInterface;
import com.ibm.pi.libertycar.driver.VirtualPWMInterface;
import com.ibm.pi.libertycar.security.AlwaysAuthenticatedUserIdManager;
import com.ibm.pi.libertycar.security.ListBasedUserIdManager;
import com.ibm.pi.libertycar.security.UserIdManager;

/**
 * Application Lifecycle Listener implementation class StateListener
 *
 */
@WebListener
public class StateListener implements ServletContextListener {

    private static CarControllerInterface carControl;

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce) {
        carControl.setCarStop();
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce) {
        if (carControl == null) {
            PWMInterface pwmInterface = null;
            try {
                pwmInterface = new PhysicalPWMInterface();
            } catch (PWMInterfaceUnavailableException e) {
                pwmInterface = new VirtualPWMInterface();
            }

            carControl = new ThreadBasedCarController(pwmInterface);
        }

        CarControlEndpoint.setControl(carControl);
        CarConfig.setControl(carControl);
        Globals.setController(carControl);
        
        UserIdManager uim;
        try {
          uim = new ListBasedUserIdManager();
          Globals.setUserIdManager(uim);
        } catch (IOException e) {
          e.printStackTrace();
          uim = new AlwaysAuthenticatedUserIdManager();
        }

        Globals.setUserIdManager(uim);
    }

}

package com.ibm.pi.libertycar.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ibm.pi.libertycar.config.Globals;
import com.ibm.pi.libertycar.control.CarController;
import com.ibm.pi.libertycar.driver.PWMInterface;
import com.ibm.pi.libertycar.driver.PhysicalPWMInterface;
import com.ibm.pi.libertycar.driver.VirtualPWMInterface;

/**
 * Application Lifecycle Listener implementation class StateListener
 *
 */
@WebListener
public class StateListener implements ServletContextListener {

    private static CarController carControl;

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

            carControl = new CarController(pwmInterface);
        }

        CarControlEndpoint.setControl(carControl);
        CarConfig.setControl(carControl);
        Globals.setController(carControl);
    }

}

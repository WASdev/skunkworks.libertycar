package com.ibm.pi.libertycar.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
			carControl = new CarController();
		}
		
		CarControlEndpoint.setControl(carControl);
		CarConfig.setControl(carControl);		
    }
	
}

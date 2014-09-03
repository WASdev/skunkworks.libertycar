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
	private static ArduinoPoller arduinoPoller;

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce) {
    	carControl.setCarStop();
    	arduinoPoller.setRed();
    	arduinoPoller.stopThread();
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
		
		if (arduinoPoller == null) {
			try{
				arduinoPoller = new ArduinoPoller();
			} catch (Exception e){
				System.err.println("Error with arduino initialisation.");
				e.printStackTrace();
			}
		}
		
		//TODO setup automated loading of config for frequencies
	//	CarConfig.createFrequencyMappingFile();// this is debug. Should be read file, and higher up in execution
    }
	
}

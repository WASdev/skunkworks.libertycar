package com.ibm.pi.libertycar.webapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ibm.wasdev.arduino.Arduino;
import com.ibm.wasdev.arduino.ArduinoService;

/**
 * Note that this file is very WIP and has been mostly disabled due to sensor issues.
 * 
 * TODO: version 1.1 needs this updating to use recommended sensor config.
 * 
 * @author Tom
 *
 */
public class ArduinoPoller implements Runnable{

	private Thread pollThread;
	private static Arduino lightArduino;
	private static int lapNumber = 0;

	private static boolean activateChecker = true;

	public ArduinoPoller(){
		if(lightArduino==null){
			try {
				lightArduino = ArduinoService.get("carduino");
				lightArduino.clearCallbacks();
				lightArduino.invoke("setWhite");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException iae){
				System.err.println("Could not detect Arduino. Arduino sensor functionality disabled.");	
				iae.printStackTrace();
				activateChecker = false;
			}
		}
		
		if(pollThread==null){
			pollThread = new Thread(this);
			pollThread.start();
		}
	}

	@Override
	public void run() {
		while(activateChecker){
			
			int crossedLine = 0;
			try {
				crossedLine = lightArduino.invoke("getCrossedLine");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException iae){
				System.err.println("Could not detect Arduino. Arduino sensor functionality disabled.");
				activateChecker = false;
			}
			if(crossedLine == 1){
				addLap();
				if(!CarConfig.getCommandURL().equals("")){
					//we need to tell someone about the lap
					try {
						String crossedLineURL = CarConfig.getCommandURL()+"/api/cars/"+CarConfig.getCarID()+"/gonepastline";
						URL passedLapURL = new URL (crossedLineURL);
						HttpURLConnection connection = (HttpURLConnection) passedLapURL.openConnection();
						connection.setRequestMethod("POST");
						connection.connect();
						connection.getResponseCode();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				//nothing yet
			}
						
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void resetLap(){
		lapNumber = 0;
	}

	public static int getLap(){
		return lapNumber;
	}

	public static void addLap(){
		lapNumber++;
	}

	public void stopThread(){
		activateChecker = false;
		pollThread.interrupt();
	}

	public static void resumeThread(){
		activateChecker = true;
	}

	public static boolean getRunning(){
		return activateChecker;
	}

	public void setRed(){
		try {
			if(activateChecker){
				lightArduino.invoke("setRed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public interface Callback {

	    /**
	     * Called when the condition of the Callback is triggered
	     * 
	     * @param value  the value that caused the callback to be triggered
	     */
	    void triggered(int value);

	    /**
	     * Called when the condition of the Callback is reset
	     * 
	     * @param value  the value that caused the callback to be reset
	     */
	    void reset(int value);
	}
	
}

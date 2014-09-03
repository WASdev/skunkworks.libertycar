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

	private long timeOverrideStarted = 0;
//	private long maxOverrideTime = 4000;
	private boolean overriding = false;
//	private com.ibm.ws.arduino.Callback crashCallback = new CrashCallback();

	private double oldMaxSpeed = 40;//TODO HARD CODED

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
				activateChecker = false;
			}
		}
//		if(distanceArduino==null){
//			try {
//				distanceArduino = ArduinoService.get("carduino_distance");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}    
//		}
//		try { TODO CALLBACK
//			lightArduino.invokeCallback("getDistance", Comparitor.LT , 50, avoidCrash());
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
		if(pollThread==null){
			pollThread = new Thread(this);
			pollThread.start();
		}
	}

	@Override
	public void run() {
		while(activateChecker){
			
			int crossedLine = 0;
			int distance = 3000;
			try {
				crossedLine = lightArduino.invoke("getCrossedLine");
				distance = lightArduino.invoke("getDistanceSafe");
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
//						System.out.println("crossed line - url is: "+crossedLineURL);
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
			
			distanceChecker(distance);		
			
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
	
//	public class CrashCallback implements com.ibm.ws.arduino.Callback{
//		@Override
//		public void reset(int value) {			
//		}
//
//		@Override
//		public void triggered(int proximity) {
//			System.out.println("called back! - distance is "+proximity);
//			if(timeOverrideStarted>System.currentTimeMillis()){
//			} else {
//				if(!overriding){
//					oldMaxSpeed = CarController.getMaxSpeed();
//					System.out.println("old max speed set to "+oldMaxSpeed);
					//if not already overriding give user some control
//					timeOverrideStarted = System.currentTimeMillis();
//					overriding = true;
//				} else if(timeOverrideStarted+maxOverrideTime<System.currentTimeMillis()){
					//System.err.println("override time out");
					//we hit timeout for max override, stop overriding
//					overriding = false;
//					CarController.overrideSpeed(false, 0);
//					CarController.overrideSteering(false, 0);
//					CarController.setMaxSpeed(oldMaxSpeed);
					//when we remove override give person grace period where it won't bother them
					//timeOverrideStarted = System.currentTimeMillis()+maxOverrideTime;
//
//				} 
//
//				if(overriding){
//					if(proximity<40){
//						System.err.println("close..");
//						CarController.setMaxSpeed(25);
//
//						if(proximity<20){
//							CarController.setMaxSpeed(0);

//							System.err.println("too close! BREAK!");
//							if(CarController.getSpeedTarget()>0){
//								CarController.setMaxSpeed(0);
								//too fast, slow down
//								CarController.overrideSpeed(true, 0);
//							}
//						} else {
//							System.err.println("overriding speed to slower...");
//							if(CarController.getSpeedTarget()>20){
//								too fast, slow down
//								CarController.overrideSpeed(true, 10);
//								if steering, make steering stronger
//								if(CarController.getSteerTarget()<0){
//									System.err.println("STEERING LEFT");
//									CarController.overrideSteering(true, -100);
//								} else if(CarController.getSteerTarget()>0){
//									System.err.println("STEERING RIGHT");
//									CarController.overrideSteering(true, 100);
//								}
//							}
//						}
//					}
//					if(proximity>40){
//						CarController.overrideSpeed(false, 0);
//						CarController.overrideSteering(false, 0);
//					}
//				}
//			}
//			
//		}		
//	}

//	/**
//	 * Callback called if we get too close to another object.
//	 * @return 
//	 * @throws IOException 
//	 */
//	private com.ibm.ws.arduino.Callback avoidCrash() throws IOException{
//		return crashCallback;
//	}
	
	private void distanceChecker(int proximity){
		
		if(!overriding){
//			oldMaxSpeed = CarController.getMaxSpeed();
//			System.out.println("old max speed set to "+oldMaxSpeed);
			//if not already overriding give user some control
			timeOverrideStarted = System.currentTimeMillis();
			overriding = true;
//		} else if(timeOverrideStarted+maxOverrideTime<System.currentTimeMillis()){
			//System.err.println("override time out");
			//we hit timeout for max override, stop overriding
//			overriding = false;
//			CarController.overrideSpeed(false, 0);
//			CarController.overrideSteering(false, 0);
//			CarController.setMaxSpeed(oldMaxSpeed);
			//when we remove override give person grace period where it won't bother them
			//timeOverrideStarted = System.currentTimeMillis()+maxOverrideTime;

		} 

		if(overriding){
			if(proximity<60){
				System.err.println("close..");
				CarController.setMaxSpeed(25);

				if(proximity<15){
					CarController.setMaxSpeed(0);

//					System.err.println("too close! BREAK!");
//					if(CarController.getSpeedTarget()>0){
//						//too fast, slow down
//						CarController.overrideSpeed(true, 0);
//					}
				} else {
					if(CarController.getSpeedTarget()>20){
						CarController.setMaxSpeed(10);

						//too fast, slow down
//						CarController.overrideSpeed(true, 10);
						//if steering, make steering stronger
//						if(CarController.getSteerTarget()<0){
//							System.err.println("STEERING LEFT");
//							CarController.overrideSteering(true, -100);
//						} else if(CarController.getSteerTarget()>0){
//							System.err.println("STEERING RIGHT");
//							CarController.overrideSteering(true, 100);
//						}
					}
				}
			}
			if(proximity>60 && overriding){
//				CarController.overrideSpeed(false, 0);
//				CarController.overrideSteering(false, 0);
				//fix max speed setting
//				if(overriding){
//					if(timeOverrideStarted+maxOverrideTime<System.currentTimeMillis()){
						overriding = false;
//						if(oldMaxSpeed<50){ // HARD CODED
							CarController.setMaxSpeed(oldMaxSpeed);//HARD CODED
//						} else {
//							CarController.setMaxSpeed(50);//HARD CODED
//						}

//					}
//				}
			}
			
		}
	}
}

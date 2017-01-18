package com.ibm.pi.libertycar.webapp;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.ibm.pi.libertycar.control.CarControllerInterface;

@ServerEndpoint("/control")
public class CarControlEndpoint  {
	
	private Logger logger = Logger.getLogger("CarEndpointLogger");
	
	private static CarControllerInterface carControl;
	
	private String userId;	
	private Session mySession;
	
	private volatile int lastTurn = 0;
	private volatile int lastThrottle = 0;
	
	private volatile String lastMessageGroup = null;

	public static void setControl(CarControllerInterface control){
		carControl = control;
		
	}
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) {
		//create new session ID for this client and put it in the map
		mySession = session;
	}
	
	@OnMessage
	public void receiveMessage(String message) {
		logger.log(Level.FINE, "Incoming Message logged: " + message);
		String returnMessage = "Unexplained error - see server logs.";
		String currentMessageGroup = null;
		try {
			ControlInstruction userData = parseMessage(message);
    		currentMessageGroup = userData.getMsggrp();
			returnMessage = parseControlsAndReturnMessage(userData);
		} catch (IOException e) {
			e.printStackTrace();
			returnMessage = "Unrecognised instruction encountered. Command ignored. Message was " + message;
		} finally {
			if (currentMessageGroup == null || !currentMessageGroup.equalsIgnoreCase(lastMessageGroup)) {
				mySession.getAsyncRemote().sendText(returnMessage);
			}
			lastMessageGroup = currentMessageGroup;
		}
	}
	
	private ControlInstruction parseMessage(String message) throws IOException {
		Gson gso = new Gson();
		return gso.fromJson(message, ControlInstruction.class);
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
	}
	
	@OnError
	public void onError(Throwable t) {
		System.err.println("An error has occurred in the websocket communication. See below for details.");
		t.printStackTrace();
	}

	public CarControllerInterface getController(){
		return carControl;
	}
	
	private String parseControlsAndReturnMessage(ControlInstruction userData) {

		int throttle = lastThrottle ;
		Long recievedThrottle = userData.getThrottle();
		if (recievedThrottle != null) {
			throttle = recievedThrottle.intValue();
			lastThrottle = throttle;
		}
		
		int turning = lastTurn;
		Long recievedTurning = userData.getTurning();
		if (recievedTurning != null) {
			turning = recievedTurning.intValue();
			lastTurn = turning;
		}
		String id = CarAdmin.getIdFromIp((String) userData.getId());
		return parseControlRequest(throttle, turning, id);
	}
	
	private String parseControlRequest(int throttle, int turning, String id){
		userId = id;
		if(userId == null){
			return "Generating ID. Please wait.";
		}
		String controlMessage = "";
		boolean noIpCheck = CarAdmin.lengthOfId < 0;
		if(noIpCheck||CarAdmin.controllerIds.contains(userId)){

			if(noIpCheck||CarAdmin.throttleControl.contains(userId)){
				carControl.setSpeed(throttle);
				controlMessage = "You are the speed - your ID is "+userId;
			}

			if(noIpCheck||CarAdmin.steeringControl.contains(userId)){
				carControl.setSteering(turning);
				controlMessage = "You are the steering - your ID is "+userId;
			}

			if(noIpCheck||(CarAdmin.throttleControl.contains(userId)&&CarAdmin.steeringControl.contains(userId))){
				controlMessage = "You are in full control - your ID is "+userId;
			}

		} else if(!CarConfig.getCommandURL().equals("") && CarAdmin.idsToForward.contains(userId)){
			controlMessage = CarConfig.getCommandURL()+"/leaderboard";
			CarAdmin.idsToForward.remove(userId);
		} else {
			controlMessage = ("You do not have control. Your ID is "+userId);
		}
		return controlMessage;
	}
}

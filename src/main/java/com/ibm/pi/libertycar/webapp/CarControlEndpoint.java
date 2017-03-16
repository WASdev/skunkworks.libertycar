package com.ibm.pi.libertycar.webapp;

import java.io.IOException;
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
import com.ibm.pi.libertycar.rest.admin.User;
import com.ibm.pi.libertycar.security.UserId;

@ServerEndpoint("/control")
public class CarControlEndpoint  {
	
	private Logger logger = Logger.getLogger("CarEndpointLogger");
	
	private static CarControllerInterface carControl;
	
	private Session mySession;
	
	private volatile Long lastTurn = Long.valueOf(0);
	private volatile Long lastThrottle = Long.valueOf(0);
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
		if (userData.getThrottle() == null) {
			userData.setThrottle(lastThrottle);
		} else {
			lastThrottle = userData.getThrottle();
		}
		
		if (userData.getTurning() == null) {
			userData.setTurning(lastTurn);
		} else {
			lastTurn = userData.getTurning();
		}
		
//		userData.setId(CarAdmin.getIdFromIp((String) userData.getId()));
		UserId user = new UserId(userData.getId());
		userData.setId(user.getUserName());
		return parseControlRequest(userData);
	}
	
	private String parseControlRequest(ControlInstruction instruction) {
		String controlMessage = "";
		boolean noIpCheck = CarAdmin.lengthOfId < 0;
		if(noIpCheck||CarAdmin.controllerIds.contains(instruction.getId())){

			if(noIpCheck||CarAdmin.throttleControl.contains(instruction.getId())){
				carControl.setSpeed(instruction.getThrottle().intValue());
				controlMessage = "You are the speed - your ID is "+instruction.getId();
			}

			if(noIpCheck||CarAdmin.steeringControl.contains(instruction.getId())){
				carControl.setSteering(instruction.getTurning().intValue());
				controlMessage = "You are the steering - your ID is "+instruction.getId();
			}

			if(noIpCheck||(CarAdmin.throttleControl.contains(instruction.getId())&&CarAdmin.steeringControl.contains(instruction.getId()))){
				controlMessage = "You are in full control - your ID is "+instruction.getId();
			}

		} else if(!CarConfig.getCommandURL().equals("") && CarAdmin.idsToForward.contains(instruction.getId())){
			controlMessage = CarConfig.getCommandURL()+"/leaderboard";
			CarAdmin.idsToForward.remove(instruction.getId());
		} else {
			controlMessage = ("You do not have control. Your ID is "+instruction.getId());
		}
		return controlMessage;
	}
}

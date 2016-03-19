package com.ibm.pi.libertycar.webapp;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import com.ibm.json.java.JSONObject;



@ServerEndpoint("/control")
public class CarControlEndpoint  {
	
	private static CarController carControl;
	
	private String userId;	
	private Session mySession;

	public static void setControl(CarController control){
		carControl = control;
	}
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) {
		//create new session ID for this client and put it in the map
		mySession = session;
	}
	
	@OnMessage
	@Consumes(MediaType.APPLICATION_JSON)
	public void receiveMessage(String message) {
		String returnMessage = "Unexplained error - see server logs.";
		try {
			returnMessage = parseControlsAndReturnMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			returnMessage = "IOException encountered. Control lost.";
		} finally {
			mySession.getAsyncRemote().sendText(returnMessage);
		}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
	}
	
	@OnError
	public void onError(Throwable t) {
		System.err.println("An error has occurred in the websocket communication. See below for details.");
		t.printStackTrace();
	}

	public CarController getController(){
		return carControl;
	}
	
	private String parseControlsAndReturnMessage(String inboundContent) throws IOException{
		JSONObject userData = JSONObject.parse(inboundContent);
		int throttle = ((Long)userData.get("throttle")).intValue();
		int turning = ((Long) userData.get("turning")).intValue();
		String id = CarAdmin.getIdFromIp((String) userData.get("id"));
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

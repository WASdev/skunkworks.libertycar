package com.ibm.pi.libertycar.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ibm.pi.libertycar.config.Globals;

@WebServlet("/admin")
public class CarAdmin extends HttpServlet {
	
	private static final long serialVersionUID = -2482544175183401161L;
	public volatile static ArrayList<String> controllerIds = new ArrayList<String>();
	public volatile static ArrayList<String> steeringControl = new ArrayList<String>();
	public volatile static ArrayList<String> throttleControl = new ArrayList<String>();
	public volatile static ArrayList<String> idsToForward = new ArrayList<String>();
	private String controllerId="";
	public static int lengthOfId=-1;
	
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String maxSpeed = req.getParameter("maxSpeed");
		String controller = req.getParameter("controller");
		String throttle = req.getParameter("throttle");
		String steering = req.getParameter("steering");
		String idLen = req.getParameter("idLen");
		String remove = req.getParameter("remove");
		String removeAll = req.getParameter("removeAll");

		if(removeAll!=null){
			idsToForward.addAll(controllerIds);
			controllerIds.clear();
			steeringControl.clear();
			throttleControl.clear();
		}

		if(remove!=null&&!!!remove.equals("none")){
			idsToForward.add(remove);
			if(controllerIds.contains(remove)){
				controllerIds.remove(remove);
			}
			if(steeringControl.contains(remove)){
				steeringControl.remove(remove);
			}
			if(throttleControl.contains(remove)){
				throttleControl.remove(remove);
			}
		}

		if(maxSpeed!=null&&maxSpeed.length()>0){
			try{
				int maxSpd = Integer.parseInt(maxSpeed);
				Globals.getController().setMaxSpeed(maxSpd);
			} catch (NumberFormatException e){
				//do nothing
			}
		}

		//add general car control
		if(controller!=null&&controller.length()>0){
			controllerId = req.getParameter("controller");
			if(!!!controllerIds.contains(controllerId)){
				controllerIds.add(controllerId);
			}
		}

		//add throttle control
		if(throttle!=null){
			if(!!!throttleControl.contains(controllerId)){
				throttleControl.add(controllerId);
			}
		}

		//add steering control
		if(steering!=null){
			if(!!!steeringControl.contains(controllerId)){
				steeringControl.add(controllerId);
			}
		}

		if(idLen!=null&&idLen.length()>0){
			int idLength = lengthOfId;
			try{
				idLength = Integer.parseInt(idLen);
			} catch (NumberFormatException e){
				//do nothing
			}
			if(lengthOfId!=idLength){
				//we have changed the id length. All existing permitted IDs are now invalid.
				//wipe all ID lists.
				controllerIds.clear();
				steeringControl.clear();
				throttleControl.clear();
				idsToForward.clear();
			}
			lengthOfId = idLength;
		}

		int carMaxSpeed= (int)Globals.getController().getMaxSpeed();		

		PrintWriter writer = resp.getWriter();
		writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		writer.println("<title>Liberty car admin panel</title>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("<form  action=\"./admin\">");
		writer.println("Car max speed: <input type=\"text\" name=\"maxSpeed\" value=\""+carMaxSpeed+"\"><br>");
		writer.println("User ID length: <input type=\"text\" name=\"idLen\" value=\""+lengthOfId+"\"><br>");
		writer.println("User ID in control: <input type=\"text\" name=\"controller\" value=\""+""+"\"><br>");
		writer.println("Throttle: <input type=\"checkbox\" name=\"throttle\" checked=\"checked\"><br>");
		writer.println("Steering: <input type=\"checkbox\" name=\"steering\" checked=\"checked\"><br>");
		writer.println("<br/>");
		writer.println("Remove control from: <br/><br/>");

		writer.println("<select name=\"remove\">");
		writer.println("<option value=\"none\">none</option>");
		for(String id:controllerIds){
			writer.println("<option value=\""+id+"\">"+id+"</option>");
		}
		writer.println("</select>");
		writer.println("<br/>");
		writer.println("<br/>");
		writer.println("<input type=\"submit\" value=\"Submit\">");

		writer.println("</form>");
		writer.println("</body>");
		writer.println("</html>");
	}
	
	/**
	 * Converts an IP address to an ID based on the ID length setting
	 * 
	 * @param ip
	 * @return
	 */
	public static String getIdFromIp(String ip){
		if(lengthOfId>0){//if id = 0 then don't substring it
			if(lengthOfId<=ip.length()){
				ip = ip.substring(ip.length()-lengthOfId, ip.length());				
			}
		}
		return ip;
	}
}

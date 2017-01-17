package com.ibm.pi.libertycar.webapp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.pi.libertycar.control.CarController;

/**
 * Servlet implementation class Config
 */
@WebServlet("/config")
public class CarConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//steering
	private int leftMax = 430;
	private int steerNeutral = 350;
	private int rightMax = 280;
	private double steeringIncrement = -1;
	private int steeringTest = 0;

	//speed
	private int maxForward = 635;
	private int speedNeutral = 405;
	private int maxReverse = 188;
//	private double speedIncrement = 2.3;
	private int speedTest = 0;

	//command app
	public static String commandURL = "";
	public static String carID = "";

	private static CarController carController;


	public static void setControl(CarController control){
		carController = control;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//analyse received data (if any)

		//see if test data received
		String steeringTestValue = req.getParameter("steeringTest");
		String speedTestValue = req.getParameter("speedTest");

		int testTimeout = 5;

		if(steeringTestValue!=null || speedTestValue!=null){
			//test mode
			if(steeringTestValue!=null && !steeringTestValue.equals("0")){
				carController.testSteering(Integer.parseInt(steeringTestValue), testTimeout);
			} else if(speedTestValue!=null && !speedTestValue.equals("0")){
				carController.testSpeed(Integer.parseInt(speedTestValue), testTimeout);
			}

		} else {

			//new command app URL
			String commandURLValue = req.getParameter("commandURL");
			if(commandURLValue!=null){
				commandURL = commandURLValue;
				System.out.println("command url set to: "+commandURL);
			}
			//car ID
			String carIDValue = req.getParameter("carID");
			if(carIDValue!=null){
				carID = carIDValue;
				System.out.println("car id set to: "+carID);
			}

			//steering
			String leftMaxValue = req.getParameter("leftMax");
			String steerNeutralValue = req.getParameter("steerNeutral");
			String rightMaxValue = req.getParameter("rightMax");

			if(leftMaxValue!=null){
				leftMax = Integer.parseInt(leftMaxValue);
			}

			if (steerNeutralValue!=null) {
				steerNeutral = Integer.parseInt(steerNeutralValue);
			}

			if(rightMaxValue!=null){
				rightMax = Integer.parseInt(rightMaxValue);
			}

			//speed
			String maxForwardValue = req.getParameter("maxForward");
			String speedNeutralValue = req.getParameter("speedNeutral");
			String maxReverseValue = req.getParameter("maxReverse");

			if(maxForwardValue!=null){
				maxForward = Integer.parseInt(maxForwardValue);
			}

			if(speedNeutralValue!=null){
				speedNeutral = Integer.parseInt(speedNeutralValue);
			}

			if(maxReverseValue!=null){
				maxReverse = Integer.parseInt(maxReverseValue);
			}
		}

		//calc increments
		steeringIncrement = (rightMax-leftMax)/100;
//		speedIncrement = (maxForward-maxReverse)/200; //200% is actually max speed (but we hide that as it is normally too high)

		//set values in car controller
		//steering
//		carController.setSteerLeft(leftMax);
//		carController.setSteerNeutral(steerNeutral);
//		carController.setSteerRight(rightMax);
		carController.setSteerInc(steeringIncrement);

		//speed
//		carController.setSpeedMax(maxForward);
//		carController.setSpeedNeutral(speedNeutral);
//		carController.setSpeedMin(maxReverse);
//		carController.setForwardSpeedInc(speedIncrement);
//		carController.setReverseSpeedInc(speedIncrement);

		//show content
		PrintWriter writer = resp.getWriter();
		writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		writer.println("<title>Liberty car configuration panel</title>");
		writer.println("</head>");
		writer.println("<body>");

		//configuration
		writer.println("<h1>Car frequency ranges</h1>");

		writer.println("<form  action=\"./config\">");

		//steering settings
		writer.println("Max left frequency: <input type=\"text\" name=\"leftMax\" value=\""+leftMax+"\"><br>");
		writer.println("Straight ahead frequency: <input type=\"text\" name=\"steerNeutral\" value=\""+steerNeutral+"\"><br>");
		writer.println("Max right frequency: <input type=\"text\" name=\"rightMax\" value=\""+rightMax+"\"><br>");

		//speed settings
		writer.println("Car max forwards frequency: <input type=\"text\" name=\"maxForward\" value=\""+maxForward+"\"><br>");
		writer.println("Car neutral frequency: <input type=\"text\" name=\"speedNeutral\" value=\""+speedNeutral+"\"><br>");
		writer.println("Max reverse frequency: <input type=\"text\" name=\"maxReverse\" value=\""+maxReverse+"\"><br>");

		writer.println("<br/>");
		//default settings picker
		writer.println("Use default settings: ");
		writer.println("<select name=\"remove\">");
		writer.println("<option value=\"None\">None</option>");
		writer.println("<option value=\"Big Blue\">Big Blue</option>");
		writer.println("<option value=\"Mercedes\">Mercedes</option>");
		writer.println("<option value=\"Ferrari\">Ferrari</option>");
		writer.println("<option value=\"Porsche\">Porsche</option>");
		writer.println("</select>");


		writer.println("<br/>");
		writer.println("<br/>");
		writer.println("<input type=\"submit\" value=\"Submit frequencies\">");
		writer.println("</form>");

		writer.println("<br/>");
		writer.println("<br/>");
		

		//test values to send to motors
		writer.println("<h1>Test frequencies</h1>");
		writer.println("<form  action=\"./config\">");		

		//test steering
		writer.println("Try steering frequency of: <input type=\"text\" name=\"steeringTest\" value=\""+steeringTest+"\"><br>");
		writer.println("<br/>");

		//test speed
		writer.println("Try speed frequency of: <input type=\"text\" name=\"speedTest\" value=\""+speedTest+"\"><br>");

		writer.println("<input type=\"submit\" value=\"Test frequency\">");
		writer.println("</form>");
		
		writer.println("<br/>");
		writer.println("<br/>");


		//command app settings
		writer.println("<h1>Command app settings</h1>");
		writer.println("<form  action=\"./config\">");		

		//command app inputs
		writer.println("URL for command app: <input type=\"text\" name=\"commandURL\" value=\""+commandURL+"\"><br>");
		writer.println("Car ID: <input type=\"text\" name=\"carID\" value=\""+ carID+"\"><br>");

		
		writer.println("<input type=\"submit\" value=\"Submit new URL\">");
		writer.println("</form>");

		writer.println("</body>");
		writer.println("</html>");	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// nothing to see here
	}
	
	public static String getCommandURL() {
		return commandURL;
	}

	public static String getCarID() {
		return carID;
	}
	
	//TODO save file
	public static void createFrequencyMappingFile(){
		File myFile = new File("$wlp.install.dir/hello");
		try {
			myFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//TODO load file
	public static void readFrequencyMappingFile(){
		
	}
	

}

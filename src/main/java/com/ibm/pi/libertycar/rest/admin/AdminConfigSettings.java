package com.ibm.pi.libertycar.rest.admin;

public class AdminConfigSettings {

	private double carMaxSpeed;
	private int userIdLengthCheck;
	
	public AdminConfigSettings(double carMaxSpeed, int userIdLengthCheck) {
		this.carMaxSpeed = carMaxSpeed;
		this.userIdLengthCheck = userIdLengthCheck;
	}
	
	
	public double getCarMaxSpeed() {
		return carMaxSpeed;
	}
	public int getUserIdLengthCheck() {
		return userIdLengthCheck;
	}
	
	
}

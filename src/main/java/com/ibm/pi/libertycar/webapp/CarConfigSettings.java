package com.ibm.pi.libertycar.webapp;

public class CarConfigSettings {


	//steering
	private int leftMax = 430;
	private int steerNeutral = 350;
	private int rightMax = 280;
	private double steeringIncrement = -1;

	//speed
	private int maxForward = 635;
	private int speedNeutral = 405;
	private int maxReverse = 188;
//	private double speedIncrement = 2.3;


	public int getLeftMax() {
		return leftMax;
	}
	public void setLeftMax(int leftMax) {
		this.leftMax = leftMax;
	}
	
	public int getSteerNeutral() {
		return steerNeutral;
	}
	public void setSteerNeutral(int steerNeutral) {
		this.steerNeutral = steerNeutral;
	}
	
	public int getRightMax() {
		return rightMax;
	}
	public void setRightMax(int rightMax) {
		this.rightMax = rightMax;
	}
	public double getSteeringIncrement() {
		return steeringIncrement;
	}
	public void setSteeringIncrement(double steeringIncrement) {
		this.steeringIncrement = steeringIncrement;
	}
	public int getMaxForward() {
		return maxForward;
	}
	public void setMaxForward(int maxForward) {
		this.maxForward = maxForward;
	}
	public int getSpeedNeutral() {
		return speedNeutral;
	}
	public void setSpeedNeutral(int speedNeutral) {
		this.speedNeutral = speedNeutral;
	}
	public int getMaxReverse() {
		return maxReverse;
	}
	public void setMaxReverse(int maxReverse) {
		this.maxReverse = maxReverse;
	}

}

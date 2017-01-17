package com.ibm.pi.libertycar.config;

public class FrequencySettings {
    
    private int leftMax;
    private int steeringNeutral;
    private int rightMax;
    private int maxForward;
    private int speedNeutral;
    private int maxReverse;
    public int getLeftMax() {
        return leftMax;
    }
    public void setLeftMax(int leftMax) {
        this.leftMax = leftMax;
    }
    public int getSteeringNeutral() {
        return steeringNeutral;
    }
    public void setSteeringNeutral(int steeringNeutral) {
        this.steeringNeutral = steeringNeutral;
    }
    public int getRightMax() {
        return rightMax;
    }
    public void setRightMax(int rightMax) {
        this.rightMax = rightMax;
    }
    public int getMaxForward() {
        return maxForward;
    }
    public void setMaxForward(int maxForward) {
        this.maxForward = maxForward;
    }
    @Override
    public String toString() {
        return "FrequencySettings [leftMax=" + leftMax + ", steeringNeutral=" + steeringNeutral + ", rightMax="
                + rightMax + ", maxForward=" + maxForward + ", speedNeutral=" + speedNeutral + ", maxReverse="
                + maxReverse + "]";
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

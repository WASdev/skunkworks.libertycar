package com.ibm.pi.libertycar.control;

public interface CarControllerInterface {

    /**
     * Sets the speed of the car in percent. Negative percent upto -100 is
     * reverse.
     * 
     * @param speed
     *            between 100 and -100
     */
    void setSpeed(int speed);

    /**
     * Sets the steering angle of the wheels at the front of the car. Takes a
     * percent where 0 to -100 is left and 0 to 100 is right.
     * 
     * @param turning
     *            int between -100 and 100
     */
    void setSteering(int turning);

    /**
     * Stops the car thread
     */
    void setCarStop();

    /**
     * Starts the car thread if it was stopped
     */
    void setCarStart();

    
    public void setMaxSpeed(double newMaxSpeed);
    
    public double getMaxSpeed();
    
    /**
     * Sets the max reverse speed for the car (between 0 and 200%)
     * 
     * @param maxSpeed
     */
    void setMaxReverseSpeed(int maxRevSpeed);

    double getMaxReverseSpeed();

    void testSteering(int frequency, int time);

    void testSpeed(int frequency, int time);

    void setSpeedNeutral(int rest);

    void setSpeedMin(int newSpeedRangeMin);

    void setSpeedMax(int newSpeedRangeMax);

    void setSteerNeutral(int newSteerRest);

    void setSteerLeft(int newSteerMin);

    void setSteerRight(int newSteerMax);

    void setSteerInc(double newSteerInc);

    void setForwardSpeedInc(double newForwardSpeedInc);

    void setReverseSpeedInc(double newReverseSpeedInc);

}
package com.ibm.pi.libertycar.control.threaded;

import java.io.IOException;

import com.ibm.pi.libertycar.control.CarControllerInterface;
import com.ibm.pi.libertycar.driver.PWMInterface;

public class ThreadBasedCarController implements Runnable, CarControllerInterface {
    // comms info
    private volatile int ticksTillStop = 0;
    private final int ticksTimeout = 10;// ticks until dead-man's handle kicks
                                        // in
    private volatile boolean run = true;
    private int timesToRunPerSecond = 20;
    private int sleepSize = 1000 / timesToRunPerSecond;

    private static double currentMaxSpeed = 50;

    // Car info
    private static int speedRest = 1620 / 4;
    private static int speedRangeMin = (speedRest - (920 / 4));
    private static int speedRangeMax = (speedRest + (920 / 4));// 920 for all
                                                               // but ferrari

    private int steerRest = 350;

    private int steerMin = 280;
    private int steerMax = 430; // 280 for ferrari, 430 for others
    private int steerRange = (steerMax - steerMin);
    private double steerInc = ((steerRange / 2) / 100) * -1;// used to be 3.2
    private int currentSteer;
    private static volatile int steerTarget = 0;
    private int steeringMaxIncrement = 100 / 4;

    private int speedMin = (speedRest + (54 / 4));

    private final static int forwardIncDivision = 200;
    private final static int reverseIncDivision = 110;

    private static double defaultForwardSpeedInc = ((speedRangeMax - speedRest) / forwardIncDivision);// used
                                                                                                      // to
                                                                                                      // be
                                                                                                      // 9.2
    private static double defaultReverseSpeedInc = ((speedRangeMin - speedRest) / reverseIncDivision) * -1;// used
                                                                                                           // to
                                                                                                           // be
                                                                                                           // -9.2

    private static double forwardSpeedInc = ((speedRangeMax - speedRest) / forwardIncDivision) * 0.25;// used
                                                                                                      // to
                                                                                                      // be
                                                                                                      // 9.2
    private static double reverseSpeedInc = (((speedRangeMin - speedRest) / reverseIncDivision) * -1) * 0.25;// used
                                                                                                             // to
                                                                                                             // be
                                                                                                             // -9.2

    private static volatile int speedTarget = 0;
    private boolean reversing = false;
    private long reverseRequest = 0;
    private int reverseTimeout = 1000;

    // PWM info
    private int steering = 14;
    private int motor = 15;

    private PWMInterface pwmInterface;

    private Thread controlThread;

    // override info
    private static boolean overrideSteering = false;
    private static boolean overrideSpeed = false;

    public ThreadBasedCarController(PWMInterface pwmInterface) {
        if (steerInc < 1 && steerInc >= 0) {
            steerInc = 1;
        } else if (steerInc < 0 && steerInc > -1) {
            steerInc = -1;
        }

        this.pwmInterface = pwmInterface;
        if (controlThread == null) {
            controlThread = new Thread(this);
            controlThread.start();
        }
        // run=true;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSpeed(int)
     */
    @Override
    public void setSpeed(int speed) {
        // if we want to reverse, let them
        if (speed < 0 || !overrideSpeed) {
            ticksTillStop = ticksTimeout;
            if (speed < -100) {
                speed = -100;
            } else if (speed > 100) {
                speed = 100;
            } else if (speed < 10 && speed > -10) {
                speed = 0;// add deadzone for middle 10%
            }
            speedTarget = speed;
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSteering(int)
     */
    @Override
    public void setSteering(int turning) {
        if (!overrideSteering) {
            ticksTillStop = ticksTimeout;
            if (turning < -100) {
                turning = -100;
            } else if (turning > 100) {
                turning = 100;
            } else if (turning < 5 && turning > -5) {
                turning = 0;// add deadzone
            }
            if (turning != 0) {
                turning *= -1; // invert sign as left/right isn't logical. TODO
                               // comment for ferrari car as servo is
                               // electrically inverted
            }

            steerTarget = turning;
        }
    }

    private synchronized void changeSpeed() {
        double speedOffset;
        int localSpeedTarget = speedTarget;

        if (localSpeedTarget > 0) {
            speedOffset = (forwardSpeedInc * localSpeedTarget) + speedMin;
            reversing = false;
            reverseRequest = 0;
        } else if (localSpeedTarget < 0) {
            speedOffset = (reverseSpeedInc * localSpeedTarget) + speedMin;
            if (speedOffset >= 0) {
                // speedOffset*=-1;
            }
            if (!reversing) {
                if (reverseRequest != 0) {
                    if (reverseRequest + reverseTimeout <= System.currentTimeMillis()) {
                        try {
                            reverseRequest = 0;
                            pwmInterface.setPWM(motor, 0, speedRest);
                            reversing = true;
                            Thread.sleep(75);
                        } catch (IOException e) {
                            System.err.println("Setting speed of " + speedOffset + " failed");
                        } catch (InterruptedException e) {
                            System.err.println("sleep failed in reverse wait");
                        }
                    }
                } else {
                    reverseRequest = System.currentTimeMillis();
                }
            }
        } else {
            reverseRequest = 0;
            speedOffset = speedRest;
            reversing = false;
        }

        try {
            pwmInterface.setPWM(motor, 0, (int) speedOffset);
        } catch (IOException e) {
            System.err.println("Setting speed of " + speedOffset + " failed");
            e.printStackTrace();
        }
    }

    private void changeSteering() {
        int steeringDiff = (steerTarget) - (currentSteer);
        int steeringToMake = 0;
        if (steeringDiff > steeringMaxIncrement) {
            steeringToMake = currentSteer + steeringMaxIncrement;
        } else if (steeringDiff < -steeringMaxIncrement) {
            steeringToMake = currentSteer - steeringMaxIncrement;
        } else {
            steeringToMake = steerTarget;
        }
        if (steeringToMake > 100) {
            steeringToMake = 100;
        } else if (steeringToMake < -100) {
            steeringToMake = -100;
        }
        int steeringOffset = (int) (steerInc * steeringToMake) + steerRest;

        try {
            pwmInterface.setPWM(steering, 0, steeringOffset);
            currentSteer = steeringToMake;
        } catch (IOException e) {
            System.err.println("Setting steering of " + steeringOffset + " failed");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (run) {
            updateCar();
        }
    }

    private void updateCar() {
        try {
            while (run) {
                if (ticksTillStop <= 0) {
                    // stop car
                    pwmInterface.setPWM(motor, 0, speedRest);
                } else {
                    ticksTillStop--;

                    changeSpeed();

                }
                if (currentSteer != steerTarget) {
                    changeSteering();
                }
                Thread.sleep(sleepSize);
            }
        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
            try {
                pwmInterface.setPWM(motor, 0, speedRest);
                pwmInterface.setPWM(steering, 0, steerRest);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setCarStop()
     */
    @Override
    public void setCarStop() {
        run = false;
        try {
            pwmInterface.setPWM(motor, 0, speedRest);
            pwmInterface.setPWM(steering, 0, steerRest);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        controlThread.interrupt();

    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setCarStart()
     */
    @Override
    public void setCarStart() {
        run = true;
        if (!!!controlThread.isAlive() || Thread.interrupted()) {
            controlThread.start();
        }
    }

    /**
     * Sets the max speed for the car (between 0 and 200%)
     * 
     * @param newMaxSpeed
     */
    public void setMaxSpeed(double newMaxSpeed) {
        if (newMaxSpeed < 0) {
            newMaxSpeed = 0;
        } else if (newMaxSpeed > 200) {
            newMaxSpeed = 200;
        }

        if (currentMaxSpeed == newMaxSpeed) {
            return;
        }

        currentMaxSpeed = newMaxSpeed;

        forwardSpeedInc = (defaultForwardSpeedInc / forwardIncDivision) * newMaxSpeed;
        // min detectable reverse is further from rest than min forwards
        if (newMaxSpeed < 40) {
            newMaxSpeed = 40;
        }
    }

    public double getMaxSpeed() {
        return (forwardSpeedInc / defaultForwardSpeedInc) * forwardIncDivision;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setMaxReverseSpeed(int)
     */
    @Override
    public void setMaxReverseSpeed(int maxRevSpeed) {
        if (maxRevSpeed > 0) {
            maxRevSpeed = 0;
        } else if (maxRevSpeed > -50) {
            maxRevSpeed = -50;
        }
        reverseSpeedInc = (defaultReverseSpeedInc / reverseIncDivision) * maxRevSpeed;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#getMaxReverseSpeed()
     */
    @Override
    public double getMaxReverseSpeed() {
        return speedRangeMin;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#testSteering(int, int)
     */
    @Override
    public void testSteering(int frequency, int time) {
        try {
            pwmInterface.setPWM(steering, frequency, frequency);
            Thread.sleep(time * 1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#testSpeed(int, int)
     */
    @Override
    public void testSpeed(int frequency, int time) {
        try {
            pwmInterface.setPWM(motor, frequency, speedRest);
            if (time > 10 * 1000) {
                time = 10;
            }
            Thread.sleep(time * 1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSpeedNeutral(int)
     */
    @Override
    public void setSpeedNeutral(int rest) {
        speedRest = rest;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSpeedMin(int)
     */
    @Override
    public void setSpeedMin(int newSpeedRangeMin) {
        speedRangeMin = newSpeedRangeMin;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSpeedMax(int)
     */
    @Override
    public void setSpeedMax(int newSpeedRangeMax) {
        speedRangeMax = newSpeedRangeMax;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSteerNeutral(int)
     */
    @Override
    public void setSteerNeutral(int newSteerRest) {
        steerRest = newSteerRest;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSteerLeft(int)
     */
    @Override
    public void setSteerLeft(int newSteerMin) {
        steerMin = newSteerMin;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSteerRight(int)
     */
    @Override
    public void setSteerRight(int newSteerMax) {
        steerMax = newSteerMax;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setSteerInc(double)
     */
    @Override
    public void setSteerInc(double newSteerInc) {
        steerInc = newSteerInc;
        if (steerInc < 0) {
            steerInc = -1;
        } else if (steerInc < 1) {
            steerInc = 1;
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setForwardSpeedInc(double)
     */
    @Override
    public void setForwardSpeedInc(double newForwardSpeedInc) {
        forwardSpeedInc = newForwardSpeedInc;
        defaultForwardSpeedInc = newForwardSpeedInc;
    }

    /* (non-Javadoc)
     * @see com.ibm.pi.libertycar.control.CarControllerInterface#setReverseSpeedInc(double)
     */
    @Override
    public void setReverseSpeedInc(double newReverseSpeedInc) {
        reverseSpeedInc = newReverseSpeedInc;
        defaultReverseSpeedInc = newReverseSpeedInc;
    }

    /**
     * Call to override user input on steering. Once finished overriding call
     * method with false.
     */
    public static void overrideSteering(boolean override, int steering) {
        overrideSteering = override;
        if (override) {
            steerTarget = steering;
        }
    }

    /**
     * Call to override user input on speed. Once finished overriding call
     * method with false.
     */
    public static void overrideSpeed(boolean override, int speed) {
        overrideSpeed = override;
        if (override) {
            speedTarget = speed;
        }
    }

}

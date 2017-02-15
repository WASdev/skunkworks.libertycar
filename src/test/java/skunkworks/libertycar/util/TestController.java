package skunkworks.libertycar.util;

import com.ibm.pi.libertycar.control.CarControllerInterface;

public class TestController implements CarControllerInterface {

    private int currentSteering = 0;
	private int currentThrottle = 0;
	private double steeringInc;
  private int lastSteeringTestTime;
  private int lastSteeringTestFrequency;
  private int steeringTestCount = 0;

  
  
  private int lastSpeedTestTime;
  private int lastSpeedTestFrequency;
  private int speedTestCount = 0;


  
  public int getLastSteeringTestTime() {
    return lastSteeringTestTime;
  }

  public int getLastSteeringTestFrequency() {
    return lastSteeringTestFrequency;
  }

  
  
  
	@Override
    public void setSpeed(int speed) {
        currentThrottle = speed;

    }

    @Override
    public void setSteering(int turning) {
        currentSteering = turning;

    }

    @Override
    public void setCarStop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCarStart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMaxSpeed(double newMaxSpeed) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getMaxSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaxReverseSpeed(int maxRevSpeed) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getMaxReverseSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void testSteering(int frequency, int time) {
        lastSteeringTestFrequency = frequency;
        lastSteeringTestTime = time;
        steeringTestCount++;

    }

    @Override
    public void testSpeed(int frequency, int time) {
      lastSpeedTestFrequency = frequency;
      lastSpeedTestTime = time;
      speedTestCount++;

    }

    @Override
    public void setSpeedNeutral(int rest) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSpeedMin(int newSpeedRangeMin) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSpeedMax(int newSpeedRangeMax) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSteerNeutral(int newSteerRest) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSteerLeft(int newSteerMin) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSteerRight(int newSteerMax) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSteerInc(double newSteerInc) {
        this.steeringInc = newSteerInc;

    }

    @Override
    public void setForwardSpeedInc(double newForwardSpeedInc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setReverseSpeedInc(double newReverseSpeedInc) {
        // TODO Auto-generated method stub

    }

	public CarValues getCurrentValues() {
		CarValues returnedValues = new CarValues(currentSteering, currentThrottle);
		currentSteering = 0;
		currentThrottle = 0;
		return returnedValues;
	}

	public double getSteeringIncrement() {
		return steeringInc;
		
	}

  public int getSteeringTestCount() {
    return steeringTestCount ;
  }

  public int getLastSpeedTestFrequency() {
    return lastSpeedTestFrequency;
  }

  public int getSpeedTestCount() {
    return speedTestCount;
  }

  public int getLastSpeedTestTime() {
    return lastSpeedTestTime;
  }

}

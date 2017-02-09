package skunkworks.libertycar.util;

public class CarValues {

	private int throttle;
	private int steering;

	public CarValues(int currentSteering, int currentThrottle) {
		this.steering = currentSteering;
		this.throttle = currentThrottle;
	}

	public int getThrottle() {
		return throttle;
	}

	public int getSteering() {
		return steering;
	}
}

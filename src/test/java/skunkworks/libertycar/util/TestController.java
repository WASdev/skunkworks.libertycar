package skunkworks.libertycar.util;

import com.ibm.pi.libertycar.control.CarControllerInterface;

public class TestController implements CarControllerInterface {

    @Override
    public void setSpeed(int speed) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSteering(int turning) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    @Override
    public void testSpeed(int frequency, int time) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    @Override
    public void setForwardSpeedInc(double newForwardSpeedInc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setReverseSpeedInc(double newReverseSpeedInc) {
        // TODO Auto-generated method stub

    }

}

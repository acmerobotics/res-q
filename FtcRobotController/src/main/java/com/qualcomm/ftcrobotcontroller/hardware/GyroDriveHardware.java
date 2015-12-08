package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Admin on 12/3/.1515.
 */
public class GyroDriveHardware extends DriveHardware {

    public I2cGyroHardware gyroHardware;

    private TurnState turnState = TurnState.NOT_TURNING;
    private double targetHeading;
    private TurnCallback callback;

    private double setPoint = 0;

    private OpMode opMode;

    public enum TurnState {
        NOT_TURNING,
        LEFT,
        RIGHT
    }

    public interface TurnCallback {
        public void onTurnFinished();
    }

    public GyroDriveHardware(I2cGyroHardware gyroHardware) {
        this.gyroHardware = gyroHardware;
    }

    @Override
    public void init(OpMode mode) {
        super.init(mode);
        opMode = mode;
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);
        opMode.telemetry.addData("Set Point", setPoint);
        opMode.telemetry.addData("Target", targetHeading);
        opMode.telemetry.addData("Raw", gyroHardware.getHeading());
        opMode.telemetry.addData("Normalized", getNormalizedHeading());
        gyroHardware.loop(timeSinceLastLoop);
        if (
            (turnState.equals(TurnState.LEFT) && getNormalizedHeading() <= targetHeading) ||
            (turnState.equals(TurnState.RIGHT) && getNormalizedHeading() >= targetHeading)
            ) {
            stopMotors();
            turnState = TurnState.NOT_TURNING;
            if (callback != null) callback.onTurnFinished();
            callback = null;
        }
    }

    public void turnLeft(double degrees, TurnCallback cb) {
        callback = cb;
        resetHeading();
        targetHeading = -degrees;
        setMotorSpeeds(-.15, .15);
        turnState = TurnState.LEFT;
    }

    public void turnRight(double degrees, TurnCallback cb) {
        callback = cb;
        resetHeading();
        targetHeading = degrees;
        setMotorSpeeds(.15, -.15);
        turnState = TurnState.RIGHT;
    }

    public void turnLeftSync(double degrees) {
        if (!(opMode instanceof LinearRobotController)) { return; }
        turnLeft(degrees, null);
        while (isTurning()) {
            try {
                opMode.telemetry.addData("Going", "Yay!");
                ((LinearRobotController) opMode).waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                RobotLog.e(e.getMessage());
            }
        }
    }

    public void turnRightSync(double degrees) {
        if (!(opMode instanceof LinearRobotController)) { return;
        }
        turnRight(degrees, null);
        while (isTurning()) {
            try {
                ((LinearRobotController) opMode).waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                RobotLog.e(e.getMessage());
            }
        }
    }

    public boolean isTurning() {
        return !turnState.equals(TurnState.NOT_TURNING);
    }

    public double getNormalizedHeading() {
        double raw = gyroHardware.getHeading();
        raw -= setPoint;
        if (Math.abs(raw) > 180) {
            raw = 180 - raw;
        }
        return raw;
    }

    public void resetHeading() {
        setPoint = gyroHardware.getHeading();
    }

}

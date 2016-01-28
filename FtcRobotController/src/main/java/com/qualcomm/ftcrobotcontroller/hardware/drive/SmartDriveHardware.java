package com.qualcomm.ftcrobotcontroller.hardware.drive;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.IMUHardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Admin on 12/3/.1515.
 */
public class SmartDriveHardware extends HardwareInterface {
    
    public static final double TURN_SPEED = 0.9;

    public DriveHardware driveHardware;
    public IMUHardware gyroHardware;

    private TurnState turnState = TurnState.NOT_TURNING;
    private double targetHeading;
    private TurnCallback callback;

    private OpMode opMode;

    public enum TurnState {
        NOT_TURNING,
        LEFT,
        RIGHT
    }

    public interface TurnCallback {
        public void onTurnFinished();
    }

    public SmartDriveHardware(DriveHardware driveHardware, IMUHardware gyroHardware) {
        this.driveHardware = driveHardware;
        this.gyroHardware = gyroHardware;
    }

    @Override
    public void init(OpMode mode) {
        opMode = mode;
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        gyroHardware.loop(timeSinceLastLoop);
        if (
            (turnState.equals(TurnState.LEFT) && gyroHardware.getHeading() <= targetHeading) ||
            (turnState.equals(TurnState.RIGHT) && gyroHardware.getHeading() >= targetHeading)
            ) {
            driveHardware.stopMotors();
            turnState = TurnState.NOT_TURNING;
            if (callback != null) callback.onTurnFinished();
            callback = null;
        }
    }

    @Override
    public String getStatusString() {
        return "turn state: " + this.turnState.toString() + "  target: " + targetHeading;
    }

    public void turnLeft(double degrees, TurnCallback cb) {
        callback = cb;
        targetHeading = gyroHardware.getHeading() - degrees;
        driveHardware.setMotorSpeeds(-TURN_SPEED, TURN_SPEED);
        turnState = TurnState.LEFT;
    }

    public void turnRight(double degrees, TurnCallback cb) {
        callback = cb;
        targetHeading = gyroHardware.getHeading() + degrees;
        driveHardware.setMotorSpeeds(TURN_SPEED, -TURN_SPEED);
        turnState = TurnState.RIGHT;
    }

    public void turnLeftSync(double degrees) {
        if (!(opMode instanceof LinearRobotController)) { return; }
        turnLeft(degrees, null);
        while (isTurning()) {
            try {
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
}

package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Ryan on 12/6/2015.
 */
public class LineFollowingDriveHardware extends GyroDriveHardware {

    public OpticalDistanceSensor ods;

    private boolean isFollowing;
    private LineState currentState;

    public double previousError = 0.0;
    public double integral = 0.0;
    public double setPoint = 0.0;
    public double kP = 0.0;
    public double kI = 0.0;
    public double kD = 0.0;

    public enum LineState {
        SWEEP,
        STRAIGHT
    }

    @Override
    public void init(OpMode mode) {
        super.init(mode);

        ods = mode.hardwareMap.opticalDistanceSensor.get("ods");
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        if (isFollowing) {
            switch(currentState) {
                case SWEEP:

                    break;
                case STRAIGHT:
                    double error = setPoint - gyroHardware.getHeading();
                    integral = integral + error * timeSinceLastLoop;
                    double derivative = (error - previousError) / timeSinceLastLoop;
                    double output = kP * error + kI * integral + kD * derivative;
                    previousError = error;
                    setMotorSpeeds(20 + output, 20 - output);
                    break;
            }
        }
    }

    public void startFollowing() {
        isFollowing = true;
        currentState = LineState.SWEEP;
    }
}

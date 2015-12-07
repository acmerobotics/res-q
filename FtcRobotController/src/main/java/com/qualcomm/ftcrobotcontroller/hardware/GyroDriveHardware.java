package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 12/3/2015.
 */
public class GyroDriveHardware extends DriveHardware {

    public I2cGyroHardware gyroHardware;

    private boolean turning = false;
    private double targetHeading;
    private TurnCallback callback;

    public interface TurnCallback {
        public void onTurnFinished();
    }

    @Override
    public void init(OpMode mode) {
        super.init(mode);
        gyroHardware = new I2cGyroHardware();
        gyroHardware.init(mode);
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);
        gyroHardware.loop(timeSinceLastLoop);
        if (turning && (Math.abs(gyroHardware.getHeading() - targetHeading) < 2.5)) {
            setMotorSpeeds(0, 0);
            turning = false;
            callback.onTurnFinished();
            callback = null;
        }
    }

    public void turnLeft(double degrees, TurnCallback cb) {
        callback = cb;
        targetHeading = gyroHardware.getHeading() - degrees;
        if (targetHeading < 0.0) {
            targetHeading += 360.0;
        }
        setMotorSpeeds(-20, 20);
        turning = true;
    }

    public void turnRight(double degrees, TurnCallback cb) {
        callback = cb;
        targetHeading = gyroHardware.getHeading() + degrees;
        if (targetHeading > 360.0) {
            targetHeading -= 360.0;
        }
        setMotorSpeeds(20, -20);
        turning = true;
    }

}

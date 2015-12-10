package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Ryan on 12/9/2015.
 */
public class UltrasonicDriveHardware extends HardwareInterface {

    public static final double WIDTH = 23.0;

    private DriveHardware driveHardware;
    private UltrasonicSensor us1, us2;
    private double last1, last2;

    public UltrasonicDriveHardware(DriveHardware driveHardware) {
        this.driveHardware = driveHardware;
    }

    @Override
    public void init(OpMode mode) {
        us1 = mode.hardwareMap.ultrasonicSensor.get("us1");
        us2 = mode.hardwareMap.ultrasonicSensor.get("us2");
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);

        last1 = us1.getUltrasonicLevel();
        last2 = us2.getUltrasonicLevel();
    }

    public double getOffsetAngle() {
        double theta = Math.atan2(0.5 * WIDTH, (last1 + last2) / 2);
        return last1 > last2 ? theta : -theta;
    }

    public double getReading() {
        return (last1 + last2) / 2;
    }
}

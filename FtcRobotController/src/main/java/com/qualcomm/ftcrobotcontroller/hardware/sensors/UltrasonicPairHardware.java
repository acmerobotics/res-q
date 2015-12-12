package com.qualcomm.ftcrobotcontroller.hardware.sensors;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Ryan on 12/9/2015.
 */
public class UltrasonicPairHardware extends HardwareInterface {

    public static final double WIDTH = 29.5;

    private UltrasonicSensor us1, us2;
    private double last1, last2;

    public UltrasonicPairHardware() {
    }

    @Override
    public void init(OpMode mode) {
        us1 = mode.hardwareMap.ultrasonicSensor.get("us1");
        us2 = mode.hardwareMap.ultrasonicSensor.get("us2");
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);

        double new1 = us1.getUltrasonicLevel();
        double new2 = us2.getUltrasonicLevel();
        if (new1 != 0) {
            last1 = new1;
        }
        if (new2 != 0) {
            last2 = new2;
        }
    }

    @Deprecated
    public double getOffsetAngle() {
        double theta = Math.atan(0.5 * WIDTH / getDistance());
        return last1 > last2 ? theta : -theta;
    }

    public double getDistance() {
        return (last1 + last2) / 2;
    }

    public double getDifference() {
        return last1 - last2;
    }
}

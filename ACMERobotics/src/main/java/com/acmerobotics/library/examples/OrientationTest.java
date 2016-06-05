package com.acmerobotics.library.examples;

import android.os.SystemClock;

import com.acmerobotics.library.module.core.ResolveTo;
import com.acmerobotics.library.module.hardware.Device;
import com.acmerobotics.library.robot.RobotOpMode;
import com.acmerobotics.library.robot.RobotClass;
import com.acmerobotics.library.sensors.drivers.BNO055;
import com.acmerobotics.library.sensors.types.OrientationSensor;
import com.acmerobotics.library.vector.Vector;

@RobotClass(OrientationTest.Config.class)
public class OrientationTest extends RobotOpMode<OrientationTest.Config> {

    public static class Config {
        @Device("device")
        @ResolveTo(BNO055.class)
        OrientationSensor orientationSensor;
    }

    @Override
    public void loop() {
        Vector orientation = robot.orientationSensor.getOrientation();
        telemetry.addData("orientation", orientation.toString());
        delay(250);
    }

    public void delay(int ms) {
        SystemClock.sleep(ms);
    }

}

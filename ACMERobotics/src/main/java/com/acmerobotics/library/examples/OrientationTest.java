package com.acmerobotics.library.examples;

import android.os.SystemClock;

import com.acmerobotics.library.robot.RobotOpMode;
import com.acmerobotics.library.robot.RobotClass;
import com.acmerobotics.library.vector.Vector;

@RobotClass(OrientationTestConfig.class)
public class OrientationTest extends RobotOpMode<OrientationTestConfig> {

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

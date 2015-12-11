package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.UltrasonicHardware;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Admin on 12/10/2015.
 */
public class UltrasonicTest extends LinearRobotController {

    private UltrasonicHardware us;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        us = new UltrasonicHardware();
        registerHardwareInterface("us", us);

        waitForStart();

        while(true) {
            telemetry.addData("Angle", us.getOffsetAngle());
            telemetry.addData("Distance", us.getDistance());
            waitOneFullHardwareCycle();
        }
    }
}

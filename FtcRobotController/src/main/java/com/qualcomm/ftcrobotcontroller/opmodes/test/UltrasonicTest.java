package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Admin on 12/10/2015.
 */
public class UltrasonicTest extends LinearRobotController {

    private UltrasonicPairHardware us;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        us = new UltrasonicPairHardware();
        registerHardwareInterface("us", us);

        waitForStart();

        while(true) {
            waitOneFullHardwareCycle();
        }
    }
}

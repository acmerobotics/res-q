package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.control.RobotController;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.FlipperHardware;

/**
 * Created by Admin on 1/19/2016.
 */
public class FlipperTest extends LinearRobotController {

    private FlipperHardware flipperHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        flipperHardware = new FlipperHardware();
        registerHardwareInterface("flipper", flipperHardware);

        waitForStart();

        flipperHardware.dump();
    }
}

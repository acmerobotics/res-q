package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.PuncherHardware;

/**
 * Created by Admin on 12/11/2015.
 */
public class PuncherTest extends LinearRobotController {
    private PuncherHardware puncherHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        puncherHardware = new PuncherHardware();
        registerHardwareInterface("punch", puncherHardware);

        waitForStart();

        puncherHardware.punchLeft();

        waitMillis(3000);

        puncherHardware.punchRight();

        waitMillis(3000);
    }
}

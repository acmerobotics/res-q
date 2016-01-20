package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;

/**
 * Created by Admin on 12/10/2015.
 */
public class AutoArmTest extends LinearRobotController {

    public ArmHardware armHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        armHardware = new ArmHardware();
        registerHardwareInterface("arm", armHardware);

        waitForStart();

        while(armHardware.isBusy()) {
            //armHardware.moveToFloor();
            telemetry.addData("Pos", armHardware.getPosition());
            waitOneFullHardwareCycle();
        }

    }
}

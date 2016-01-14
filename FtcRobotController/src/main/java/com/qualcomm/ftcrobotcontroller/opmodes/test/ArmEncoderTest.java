package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.mechanisms.ArmHardware;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Admin on 12/10/2015.
 */
public class ArmEncoderTest extends LinearRobotController {

    private ArmHardware armHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        armHardware = new ArmHardware();
        registerHardwareInterface("arm", armHardware);

        waitForStart();

        double goal = 0.0;

        armHardware.setArmMode(ArmHardware.ArmMode.RUN_TO_POSITION);

        while (opModeIsActive()) {
            if (gamepad1.a) {
                goal += 10.0;
            } else if (gamepad1.b) {
                goal -= 10.0;
            }

            armHardware.setArmPosition((int) Math.round(goal));
            telemetry.addData("Position", armHardware.getPosition());
            waitOneFullHardwareCycle();
        }
    }
}

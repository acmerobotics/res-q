package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Admin on 12/10/2015.
 */
public class ArmEncoderTest extends LinearRobotController {

    private DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        motor = hardwareMap.dcMotor.get("step");

        double offset = motor.getCurrentPosition();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                motor.setPower(0.2);
            } else if (gamepad1.b) {
                motor.setPower(-0.2);
            } else {
                motor.setPower(0);
            }
            telemetry.addData("Reading", motor.getCurrentPosition() - offset);
            waitOneFullHardwareCycle();
        }
    }
}

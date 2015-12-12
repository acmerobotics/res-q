package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Admin on 12/10/2015.
 */
public class AutoStep extends LinearRobotController {

    public DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        motor = hardwareMap.dcMotor.get("step");

        int target = 550;
        int offset = motor.getCurrentPosition();

        waitForStart();

        double error;
        while (opModeIsActive()) {
            error = target - (motor.getCurrentPosition() - offset);
            motor.setPower(Range.clip(error * 0.0025, -1, 1));
            telemetry.addData("Pos", motor.getCurrentPosition() - offset);
            if (gamepad1.a) {
                target += 1;
            }
            if (gamepad1.b) {
                target -= 1;
            }

            waitOneFullHardwareCycle();
        }


    }
}

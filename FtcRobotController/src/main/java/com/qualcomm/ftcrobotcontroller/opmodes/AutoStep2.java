package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.LinearRobotController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Admin on 12/10/2015.
 */
public class AutoStep2 extends LinearRobotController {

    public DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        motor = hardwareMap.dcMotor.get("step");

        waitForStart();

        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motor.setPower(0.2);
        int target = 500;
        while (motor.getCurrentPosition() < target) {
            telemetry.addData("Pos", motor.getCurrentPosition());
            waitOneFullHardwareCycle();
        }
        motor.setPower(0);

    }
}

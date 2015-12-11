package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.DcMotorStepHardware;
import com.qualcomm.ftcrobotcontroller.hardware.LinearRobotController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Admin on 12/10/2015.
 */
public class AutoStep extends LinearRobotController {

    private DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        motor = hardwareMap.dcMotor.get("step");

        waitForStart();

        motor.setDirection(DcMotor.Direction.FORWARD);
        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        waitOneFullHardwareCycle();
        motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        /// michelle
        motor.setTargetPosition(100);

        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
       // motor.setPower(0.5);
        waitOneFullHardwareCycle();
        while (motor.isBusy()) {
           // RobotLog.d("motor is Busy!!!");
            telemetry.addData("Pos", motor.getCurrentPosition());
            telemetry.addData("Power", motor.getPower());
            waitOneFullHardwareCycle();
        }
        motor.setPower(0);
    }
}

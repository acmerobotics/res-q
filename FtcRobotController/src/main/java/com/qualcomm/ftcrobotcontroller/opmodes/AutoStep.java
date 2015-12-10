package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.DcMotorStepHardware;
import com.qualcomm.ftcrobotcontroller.hardware.LinearRobotController;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Ryan on 12/9/2015.
 */
public class AutoStep extends LinearRobotController {

    public DcMotorStepHardware stepHardware;
    public DcMotor motor;
    public int position;

    @Override
    public void runOpMode() throws InterruptedException {
        motor = hardwareMap.dcMotor.get("arm");
        stepHardware = new DcMotorStepHardware(motor);
        position = stepHardware.getCurrentPosition();
        registerHardwareInterface("step", stepHardware);

        waitForStart();

        while (true) {
            position++;
            telemetry.addData("Position", position);
            waitMillis(10);
        }
    }
}

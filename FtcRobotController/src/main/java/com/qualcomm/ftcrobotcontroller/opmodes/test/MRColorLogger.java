package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class MRColorLogger extends LinearRobotController {

    private ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        colorSensor = hardwareMap.colorSensor.get("line");
        colorSensor.setI2cAddress(0x3e);
        colorSensor.enableLed(true);

        boolean collectingData = true;

        waitForStart();

        while (collectingData && opModeIsActive()) {
            telemetry.addData("Status", "Currently logging color sensor data. Press [x] to stop.");

            if (gamepad1.x) {
                collectingData = false;
            }

            waitOneFullHardwareCycle();
        }
    }
}

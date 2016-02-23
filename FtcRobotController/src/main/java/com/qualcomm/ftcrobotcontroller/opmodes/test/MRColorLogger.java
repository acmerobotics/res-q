package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.acmerobotics.library.file.DataLogger;
import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class MRColorLogger extends LinearRobotController {

    private ColorSensor colorSensor;
    private DataLogger logger;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        colorSensor = hardwareMap.colorSensor.get("front");

        logger = new DataLogger(this, "colors.csv");
        logger.writeLine("timestamp,red,green,blue,alpha");

        boolean collectingData = true;

        waitForStart();

        while (collectingData && opModeIsActive()) {
            telemetry.addData("Status", "Currently logging color sensor data. Press [x] to stop.");

            if (gamepad1.x) {
                logger.close();
                collectingData = false;
            }

            logger.writeLine(String.format("%d,%d,%d,%d,%d",
                    System.nanoTime(),
                    colorSensor.red(),
                    colorSensor.green(),
                    colorSensor.blue(),
                    colorSensor.alpha()
            ));

            waitOneFullHardwareCycle();
        }
    }
}

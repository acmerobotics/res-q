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

        colorSensor = hardwareMap.colorSensor.get("line");
        colorSensor.setI2cAddress(0x3e);
        colorSensor.enableLed(true);

        logger = new DataLogger(this, "colors.csv");
        logger.writeLine("timestamp,red,green,blue,alpha");

        boolean collectingData = true;

        waitForStart();

        while (collectingData && opModeIsActive()) {
            telemetry.addData("Status", "Currently logging color sensor data. Press [x] to stop.");

            logger.writeLine(String.format("%d,%d,%d,%d,%d",
                    System.nanoTime(),
                    colorSensor.red(),
                    colorSensor.green(),
                    colorSensor.blue(),
                    colorSensor.alpha()
            ));

            if (gamepad1.x) {
                collectingData = false;
                logger.close();
            }

            waitOneFullHardwareCycle();
        }
    }
}

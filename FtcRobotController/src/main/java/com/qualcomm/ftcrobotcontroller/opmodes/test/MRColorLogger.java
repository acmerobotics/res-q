package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.acmerobotics.library.file.DataLogger;
import com.qualcomm.ftcrobotcontroller.control.RobotController;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class MRColorLogger extends RobotController {

    private ColorSensor colorSensor;
    private DataLogger logger;

    @Override
    public void init() {
        super.init();

        colorSensor = hardwareMap.colorSensor.get("front");

        logger = new DataLogger(this, "colors", false);
        logger.writeLine("timestamp,red,green,blue,alpha");
    }

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("Status", "Currently logging color sensor data. Press [x] to stop.");

        if (gamepad1.x) {
            logger.close();
            this.stop();
        }

        logger.writeLine(String.format("%d,%d,%d,%d,%d",
                System.nanoTime(),
                colorSensor.red(),
                colorSensor.green(),
                colorSensor.blue(),
                colorSensor.alpha()
        ));
    }
}

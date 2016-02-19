package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.control.RobotController;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Admin on 2/18/2016.
 */
public class LightSensorTest extends RobotController {

    public ColorSensor line;

    @Override
    public void init() {
        super.init();

        line = hardwareMap.colorSensor.get("line");
        line.setI2cAddress(0x3e);
    }

    @Override
    public void loop() {
        telemetry.addData("Alpha", line.alpha());
    }
}

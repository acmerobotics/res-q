package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.I2cRGBHardware;
import com.qualcomm.ftcrobotcontroller.hardware.RobotController;

/**
 * Created by Admin on 11/30/2015.
 */
public class I2cColorTest extends RobotController {

    private I2cRGBHardware rgbHardware;

    @Override
    public void init() {
        super.init();

        rgbHardware = new I2cRGBHardware();
        registerHardwareInterface("color", rgbHardware);
    }

    @Override
    public void loop() {
        super.loop();
        telemetry.addData("Clear", rgbHardware.getClear());
        telemetry.addData("Red", rgbHardware.getRed());
        telemetry.addData("Green", rgbHardware.getGreen());
        telemetry.addData("Blue", rgbHardware.getBlue());
    }

}

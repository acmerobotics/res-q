package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.hardware.sensors.ColorHardware;
import com.qualcomm.ftcrobotcontroller.control.RobotController;

/**
 * Created by Admin on 11/30/2015.
 */
public class I2cColorTest extends RobotController {

    private ColorHardware rgbHardware;

    @Override
    public void init() {
        super.init();

        rgbHardware = new ColorHardware();
        registerHardwareInterface("color", rgbHardware);
    }

    @Override
    public void loop() {
        super.loop();
    }

}

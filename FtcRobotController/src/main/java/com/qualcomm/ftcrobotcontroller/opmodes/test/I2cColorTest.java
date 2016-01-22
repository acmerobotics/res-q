package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cColorHardware;
import com.qualcomm.ftcrobotcontroller.control.RobotController;

/**
 * Created by Admin on 11/30/2015.
 */
public class I2cColorTest extends RobotController {

    private I2cColorHardware rgbHardware;

    @Override
    public void init() {
        super.init();

        rgbHardware = new I2cColorHardware();
        registerHardwareInterface("color", rgbHardware);
    }

    @Override
    public void loop() {
        super.loop();
    }

}

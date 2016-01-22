package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cGyroHardware;
import com.qualcomm.ftcrobotcontroller.control.RobotController;

/**
 * Created by Ryan on 11/29/2015.
 */
public class I2cGyroTest extends RobotController {

    private I2cGyroHardware gyroHardware;

    @Override
    public void init() {
        super.init();

        gyroHardware = new I2cGyroHardware();
        registerHardwareInterface("gyro", gyroHardware);
    }

    @Override
    public void loop() {
        super.loop();
    }

}

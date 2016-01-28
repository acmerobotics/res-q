package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.hardware.sensors.IMUHardware;
import com.qualcomm.ftcrobotcontroller.control.RobotController;

/**
 * Created by Ryan on 11/29/2015.
 */
public class I2cGyroTest extends RobotController {

    private IMUHardware gyroHardware;

    @Override
    public void init() {
        super.init();

        gyroHardware = new IMUHardware();
        registerHardwareInterface("gyro", gyroHardware);
    }

    @Override
    public void loop() {
        super.loop();
    }

}

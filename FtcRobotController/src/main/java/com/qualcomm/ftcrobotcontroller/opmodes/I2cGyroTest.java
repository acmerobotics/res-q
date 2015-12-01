package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.I2cGyroHardware;
import com.qualcomm.ftcrobotcontroller.hardware.RobotController;

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
        telemetry.addData("Heading", gyroHardware.getHeading());
    }

}

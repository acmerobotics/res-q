package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.GyroHardware;
import com.qualcomm.ftcrobotcontroller.hardware.RobotController;

/**
 * Created by Admin on 10/5/2015.
 */
public class GyroTest extends RobotController {

    private GyroHardware gyro;

    @Override
    public void init() {
        super.init();
        gyro = new GyroHardware();
        registerHardwareInterface("gyro", gyro);
    }

    @Override
    public void loop() {
        super.loop();
    }
}

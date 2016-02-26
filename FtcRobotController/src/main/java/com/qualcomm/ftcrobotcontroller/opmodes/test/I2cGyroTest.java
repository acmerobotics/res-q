package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.hardware.sensors.IMUHardware;
import com.qualcomm.ftcrobotcontroller.control.RobotController;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Ryan on 11/29/2015.
 */
public class I2cGyroTest extends RobotController {

    private GyroSensor gyroSensor;

    @Override
    public void init() {
        super.init();

        gyroSensor = hardwareMap.gyroSensor.get("gyro");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            gyroSensor.resetZAxisIntegrator();
        }

        telemetry.addData("reading", gyroSensor.getHeading());
    }

}

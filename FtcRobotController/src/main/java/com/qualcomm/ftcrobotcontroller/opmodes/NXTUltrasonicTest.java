package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.RobotController;
import com.qualcomm.ftcrobotcontroller.util.Helper;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Admin on 12/6/2015.
 */
public class NXTUltrasonicTest extends RobotController {

    public UltrasonicSensor ultrasonicSensor;

    @Override
    public void init() {
        super.init();
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ultrasonic");
    }

    @Override
    public void loop() {
        super.loop();
        telemetry.addData("Distance", ultrasonicSensor.getUltrasonicLevel());
    }
}

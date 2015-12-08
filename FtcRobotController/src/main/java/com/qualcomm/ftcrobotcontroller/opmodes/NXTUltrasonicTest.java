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

    public UltrasonicSensor us1, us2;
    public static final double DISTANCE = 26.0;

    @Override
    public void init() {
        super.init();
        us1 = hardwareMap.ultrasonicSensor.get("us1");
        us2 = hardwareMap.ultrasonicSensor.get("us2");
    }

    @Override
    public void loop() {
        super.loop();
        double val1 = us1.getUltrasonicLevel();
        double val2 = us2.getUltrasonicLevel();
        telemetry.addData("US1", val1);
        telemetry.addData("US2", val2);
        telemetry.addData("Angle", 90 + (Math.atan2(Math.abs(val1 - val2), DISTANCE) * 360 / Math.PI));
    }
}

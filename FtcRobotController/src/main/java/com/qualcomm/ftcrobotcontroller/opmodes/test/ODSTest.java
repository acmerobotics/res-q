package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Admin on 1/26/2016.
 */
public class ODSTest extends OpMode {

    private OpticalDistanceSensor ods;

    @Override
    public void init() {
        ods = hardwareMap.opticalDistanceSensor.get("ods");
    }

    @Override
    public void loop() {
        telemetry.addData("Value", ods.getLightDetected());
    }
}

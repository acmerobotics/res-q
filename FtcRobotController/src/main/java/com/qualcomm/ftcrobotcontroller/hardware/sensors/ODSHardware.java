package com.qualcomm.ftcrobotcontroller.hardware.sensors;

import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Ryan on 1/27/2016.
 */
public class ODSHardware extends HardwareInterface {

    private OpticalDistanceSensor odsSensor;

    public enum LineColor {
        LIGHT,
        DARK
    }

    @Override
    public void init(OpMode mode) {
        odsSensor = mode.hardwareMap.opticalDistanceSensor.get("ods");
    }

    @Override
    public String getStatusString() {
        return "line color: " + getLineColor().toString() + "  raw: " + odsSensor.getLightDetected();
    }

    public LineColor getLineColor() {
        return odsSensor.getLightDetected() > 0.97 ? LineColor.LIGHT : LineColor.DARK;
    }


}

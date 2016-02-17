package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.ftcrobotcontroller.Palette;
import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Admin on 2/16/2016.
 */
public class HSV extends LinearRobotController {
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        colorSensor = hardwareMap.colorSensor.get("line");
        colorSensor.setI2cAddress(0x3e);

        waitForStart();

        while (opModeIsActive()) {
            float[] hsv = new float[3];
            Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsv);
            telemetry.addData("R", colorSensor.red());
            telemetry.addData("H", hsv[0]);
            telemetry.addData("S", hsv[1]);
            telemetry.addData("V", hsv[2]);
            telemetry.addData("Color", Palette.getColorFromHSV(hsv).toString());
            waitOneFullHardwareCycle();
        }
    }
}
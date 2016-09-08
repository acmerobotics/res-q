package com.acmerobotics.library.examples;

import android.os.SystemClock;

import com.acmerobotics.library.sensors.drivers.TCS34725Chip;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;

public class ColorTest extends OpMode {

    private TCS34725Chip colorSensor;

    @Override
    public void init() {
        I2cDevice device = hardwareMap.i2cDevice.get("device");
        colorSensor = new TCS34725Chip(this, device);
        colorSensor.begin();
        colorSensor.setIntegrationTime(TCS34725Chip.IntegrationTime.INTEGRATIONTIME_50MS);
    }

    @Override
    public void loop() {
        telemetry.addData("red", colorSensor.getRed());
        telemetry.addData("green", colorSensor.getGreen());
        telemetry.addData("blue", colorSensor.getBlue());
        SystemClock.sleep(50);
    }
}

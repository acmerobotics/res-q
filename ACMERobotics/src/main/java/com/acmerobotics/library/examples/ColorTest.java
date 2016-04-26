package com.acmerobotics.library.examples;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;

import com.acmerobotics.library.R;
import com.acmerobotics.library.sensors.drivers.TCS34725;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class ColorTest extends OpMode {

    private TCS34725 colorSensor;

    @Override
    public void init() {
        I2cDevice device = hardwareMap.i2cDevice.get("device");
        I2cDeviceSynch deviceSynch = new I2cDeviceSynchImpl(device, 0x29 * 2, true);
        colorSensor = new TCS34725(this, deviceSynch);
        colorSensor.begin();
        colorSensor.setIntegrationTime(TCS34725.IntegrationTime.INTEGRATIONTIME_50MS);
    }

    @Override
    public void loop() {
        telemetry.addData("red", colorSensor.getRed());
        telemetry.addData("green", colorSensor.getGreen());
        telemetry.addData("blue", colorSensor.getBlue());
        SystemClock.sleep(50);
    }
}

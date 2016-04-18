package com.acmerobotics.library.examples;

import android.os.SystemClock;

import com.acmerobotics.library.i2c.BoschBNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class JSONI2cTest extends OpMode {

    private BoschBNO055IMU imu;

    @Override
    public void init() {
        I2cDevice device = hardwareMap.i2cDevice.get("gyro");
        I2cDeviceSynch deviceSynch = new I2cDeviceSynchImpl(device, 0x50, true);
        imu = new BoschBNO055IMU(this, deviceSynch);
    }

    @Override
    public void loop() {
        telemetry.addData("chip", imu.getName());
        telemetry.addData("chipId", imu.getExtra("id"));
        SystemClock.sleep(50);
    }
}

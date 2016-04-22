package com.acmerobotics.library.examples;

import android.os.SystemClock;

import com.acmerobotics.library.sensors.phone.InternalOrientation;
import com.acmerobotics.library.sensors.types.OrientationSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class InternalGyroTest extends OpMode {

    private OrientationSensor gyro;

    @Override
    public void init() {
        gyro = new InternalOrientation(this);
    }

    @Override
    public void loop() {
        telemetry.addData("yaw", gyro.getYaw());
        telemetry.addData("roll", gyro.getRoll());
        telemetry.addData("pitch", gyro.getPitch());
        SystemClock.sleep(50);
    }
}

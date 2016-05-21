package com.acmerobotics.library.examples;

import android.os.SystemClock;

import com.acmerobotics.library.module.hardware.HardwareInjector;
import com.acmerobotics.library.sensors.types.OrientationSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class OrientationTest extends OpMode {

    private OrientationSensor sensor;

    @Override
    public void init() {
        HardwareInjector injector = new HardwareInjector(new OrientationTestModule(this), hardwareMap);
        sensor = injector.create(OrientationSensor.class);
    }

    @Override
    public void loop() {
        telemetry.addData("orientation", sensor.getOrientation().toString());
        SystemClock.sleep(250);
    }
}

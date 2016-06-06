package com.acmerobotics.library.examples;

import com.acmerobotics.library.inject.hardware.HardwareInjector;
import com.acmerobotics.library.sensors.types.GyroSensor;
import com.acmerobotics.library.vector.Vector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class GyroTest extends OpMode {

    public GyroSensor gyro;

    @Override
    public void init() {
        HardwareInjector injector = new HardwareInjector(new GyroTestModule(this), hardwareMap);
        gyro = injector.create(GyroSensor.class);
    }

    @Override
    public void loop() {
        Vector reading = gyro.getAngularVelocity();
        telemetry.addData("Gyro", gyro.getAngularVelocity());
    }
}

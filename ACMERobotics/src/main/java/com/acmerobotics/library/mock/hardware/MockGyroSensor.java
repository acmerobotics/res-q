package com.acmerobotics.library.mock.hardware;

import com.acmerobotics.library.sensors.types.GyroSensor;

public class MockGyroSensor implements GyroSensor {

    public MockGyroSensor() {

    }

    @Override
    public double getYaw() {
        return 0;
    }

    @Override
    public double getPitch() {
        return 0;
    }

    @Override
    public double getRoll() {
        return 0;
    }
}

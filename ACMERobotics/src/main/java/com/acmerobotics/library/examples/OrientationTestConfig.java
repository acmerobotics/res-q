package com.acmerobotics.library.examples;

import com.acmerobotics.library.module.core.ResolveTo;
import com.acmerobotics.library.module.hardware.Device;
import com.acmerobotics.library.sensors.drivers.BNO055;
import com.acmerobotics.library.sensors.types.OrientationSensor;

public class OrientationTestConfig {

    @Device("device")
    @ResolveTo(BNO055.class)
    OrientationSensor orientationSensor;

}

package com.acmerobotics.library.examples;

import com.acmerobotics.library.module.hardware.Hardware;
import com.acmerobotics.library.module.core.ResolveTo;
import com.acmerobotics.library.robot.RobotConfig;
import com.acmerobotics.library.sensors.drivers.BNO055;
import com.acmerobotics.library.sensors.phone.InternalOrientation;
import com.acmerobotics.library.sensors.types.OrientationSensor;

public class OrientationTestConfig extends RobotConfig {

    @Hardware("device")
    @ResolveTo(InternalOrientation.class)
    OrientationSensor orientationSensor;

}

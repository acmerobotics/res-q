package com.acmerobotics.library.examples;

import com.acmerobotics.library.examples.BoschIMUTest;
import com.acmerobotics.library.examples.ColorTest;
import com.acmerobotics.library.examples.InternalGyroTest;
import com.acmerobotics.library.examples.InternalSensorScanner;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

public class ExampleOpModeRegister implements OpModeRegister {

    @Override
    public void register(OpModeManager manager) {
        manager.register("IMU Test", BoschIMUTest.class);
        manager.register("Color Test", ColorTest.class);
        manager.register("Internal Gyro Test", InternalGyroTest.class);
        manager.register("Internal Sensor Scanner", InternalSensorScanner.class);
    }

}

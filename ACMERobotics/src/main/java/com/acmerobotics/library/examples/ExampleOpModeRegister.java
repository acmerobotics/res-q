package com.acmerobotics.library.examples;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

public class ExampleOpModeRegister implements OpModeRegister {

    @Override
    public void register(OpModeManager manager) {
//        manager.register("Gyro Test", GyroTest.class);
//        manager.register("Color Test", ColorTest.class);
//        manager.register("Internal Sensor Scanner", InternalSensorScanner.class);
        manager.register("Camera Test", CameraTest.class);
    }

}

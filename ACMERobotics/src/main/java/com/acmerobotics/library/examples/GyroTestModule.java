package com.acmerobotics.library.examples;

import com.acmerobotics.library.inject.core.BaseModule;
import com.acmerobotics.library.inject.hardware.HardwareProvider;
import com.acmerobotics.library.sensors.drivers.BNO055;
import com.acmerobotics.library.sensors.types.GyroSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class GyroTestModule extends BaseModule {

    private OpMode opMode;

    public GyroTestModule(OpMode mode) {
        opMode = mode;
    }

    @Override
    public void configure() {
        bind(OpMode.class).toInstance(opMode);
        bind(I2cDevice.class).toProvider(new HardwareProvider("device"));
        bind(I2cDeviceSynch.class).to(I2cDeviceSynchImpl.class);
        bind(GyroSensor.class).to(BNO055.class);
        bind(int.class).toInstance(0x50);
        bind(boolean.class).toInstance(true);
        bind(BNO055.AngleUnits.class).toInstance(BNO055.AngleUnits.DEGREES);
        bind(BNO055.TemperatureUnits.class).toInstance(BNO055.TemperatureUnits.FAHRENHEIT);
        bind(BNO055.OperationMode.class).toInstance(BNO055.OperationMode.NDOF);
    }

}

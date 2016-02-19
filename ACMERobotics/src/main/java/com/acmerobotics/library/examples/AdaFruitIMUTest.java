package com.acmerobotics.library.examples;

import com.acmerobotics.library.i2c.AdaFruitBNO055;
import com.acmerobotics.library.i2c.I2cDeviceClient;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

public class AdaFruitIMUTest extends OpMode {

    public AdaFruitBNO055 imuChip;
    public DeviceInterfaceModule dim;

    @Override
    public void init() {
        dim = hardwareMap.deviceInterfaceModule.get("dim");
        imuChip = new AdaFruitBNO055(new I2cDeviceClient(dim, 0, AdaFruitBNO055.BNO055ESS_A_ADDR));
        imuChip.begin();
        imuChip.setAngleUnits(AdaFruitBNO055.AngleUnits.DEGREES);
        imuChip.setTemperatureUnits(AdaFruitBNO055.TemperatureUnits.FAHRENHEIT);
        imuChip.setMode(AdaFruitBNO055.OperationMode.NDOF);
    }

    @Override
    public void loop() {
        telemetry.addData("Euler", imuChip.getEulerAngles());
        telemetry.addData("Gyro", imuChip.getAngularVelocity());
        telemetry.addData("Accel", imuChip.getAcceleration());
        telemetry.addData("Mag", imuChip.getMagneticFlux());
        telemetry.addData("Temp", imuChip.getTemperature());
    }
}

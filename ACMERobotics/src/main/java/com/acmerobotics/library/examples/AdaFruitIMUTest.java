package com.acmerobotics.library.examples;

import com.acmerobotics.library.i2c.AdaFruitBNO055;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class AdaFruitIMUTest extends OpMode {

    public AdaFruitBNO055 imuChip;
    private int counter = 0;

    @Override
    public void init() {
        I2cDevice device = hardwareMap.i2cDevice.get("gyro");
        telemetry.addData("Conn. Info", device.getConnectionInfo());
        I2cDeviceSynch deviceSynch = new I2cDeviceSynchImpl(device, 0x50, true);
        imuChip = new AdaFruitBNO055(deviceSynch);
        imuChip.begin();
        imuChip.setAngleUnits(AdaFruitBNO055.AngleUnits.DEGREES);
        imuChip.setTemperatureUnits(AdaFruitBNO055.TemperatureUnits.FAHRENHEIT);
        imuChip.setMode(AdaFruitBNO055.OperationMode.NDOF);
    }

    @Override
    public void loop() {
//        telemetry.addData("Counter", counter++);
//        imuChip.delay(5000);
        telemetry.addData("Euler", imuChip.getEulerAngles());
        telemetry.addData("Gyro", imuChip.getAngularVelocity());
        telemetry.addData("Accel", imuChip.getAcceleration());
        telemetry.addData("Mag", imuChip.getMagneticFlux());
        telemetry.addData("Temp", imuChip.getTemperature());
        imuChip.delay(1000);
    }
}

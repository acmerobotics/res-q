package com.acmerobotics.library.examples;

import com.acmerobotics.library.i2c.BoschBNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class BoschIMUTest extends OpMode {

    public BoschBNO055IMU imuChip;
    private int counter = 0;

    @Override
    public void init() {
        I2cDevice device = hardwareMap.i2cDevice.get("gyro");
        telemetry.addData("Conn. Info", device.getConnectionInfo());
        I2cDeviceSynch deviceSynch = new I2cDeviceSynchImpl(device, 0x50, true);
        imuChip = new BoschBNO055IMU(this, deviceSynch);
        imuChip.begin();
        imuChip.setAngleUnits(BoschBNO055IMU.AngleUnits.DEGREES);
        imuChip.setTemperatureUnits(BoschBNO055IMU.TemperatureUnits.FAHRENHEIT);
        imuChip.setMode(BoschBNO055IMU.OperationMode.NDOF);
    }

    @Override
    public void loop() {
//        telemetry.addData("Counter", counter++);
//        imuChip.delay(5000);
        telemetry.addData("Euler", imuChip.getEulerAngles());
//        telemetry.addData("Gyro", imuChip.getAngularVelocity());
//        telemetry.addData("Accel", imuChip.getAcceleration());
//        telemetry.addData("Mag", imuChip.getMagneticFlux());
        telemetry.addData("Temp", imuChip.getTemperature());
        imuChip.delay(100);
    }
}

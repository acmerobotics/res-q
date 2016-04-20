package com.acmerobotics.library.examples;

import com.acmerobotics.library.sensors.drivers.BNO055;
import com.acmerobotics.library.vector.Vector;
import com.acmerobotics.library.vector.VectorIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class BoschIMUTest extends OpMode {

    public BNO055 imuChip;
    private VectorIntegrator integrator;
    private int counter = 0;

    @Override
    public void init() {
        integrator = new VectorIntegrator();
        I2cDevice device = hardwareMap.i2cDevice.get("device");
        telemetry.addData("Conn. Info", device.getConnectionInfo());
        I2cDeviceSynch deviceSynch = new I2cDeviceSynchImpl(device, 0x50, true);
        imuChip = new BNO055(this, deviceSynch);
        imuChip.begin();
        imuChip.setAngleUnits(BNO055.AngleUnits.DEGREES);
        imuChip.setTemperatureUnits(BNO055.TemperatureUnits.FAHRENHEIT);
        imuChip.setMode(BNO055.OperationMode.NDOF);
    }

    @Override
    public void loop() {
        Vector gyro = imuChip.getAngularVelocity();
        integrator.add(gyro);
        telemetry.addData("Angle", integrator.getSum());
//        telemetry.addData("Euler", imuChip.getEulerAngles());
        telemetry.addData("Gyro", imuChip.getAngularVelocity());
//        telemetry.addData("Accel", imuChip.getAcceleration());
//        telemetry.addData("Mag", imuChip.getMagneticFlux());
//        telemetry.addData("Temp", imuChip.getTemperature());
    }
}

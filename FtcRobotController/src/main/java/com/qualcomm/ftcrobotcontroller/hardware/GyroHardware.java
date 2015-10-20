package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Created by Admin on 10/5/2015.
 */
public class GyroHardware extends HardwareInterface {

    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private GyroSensor gyro;
    private double lastReading, orientation, bias = 0;
    public static final IntegrationMode MODE = IntegrationMode.TRAPEZOIDAL;

    @Override
    public void init(OpMode mode) {
        hardwareMap = mode.hardwareMap;
        telemetry = mode.telemetry;
        gyro = hardwareMap.gyroSensor.get("gyro");
        bias = 0;
        lastReading = 0;
        orientation = 0;
        generateBias(10);
    }

    public void generateBias(int numReadings) {
        double total = 0.0;
        for (int i = 0; i < numReadings; i++) {
            total += readSensor();
//            try {
//                Thread.sleep(250);
//            } catch (Exception e) {
//
//            }
        }
        bias = total / ((double) numReadings);
    }

    public double readSensor() {
        return gyro.getRotation() - bias;
    }

    @Override
    public void loop(long nsSinceLastLoop) {
        double reading = readSensor();
        switch (MODE) {
            case LEFT:
                orientation += ((double) nsSinceLastLoop) * reading * Math.pow(10.0, -9.0);
                break;
            case TRAPEZOIDAL:
                orientation += ((double) nsSinceLastLoop) * (reading + lastReading) * 0.5 * Math.pow(10.0, -9.0);
                break;
        }
        lastReading = reading;
        telemetry.addData("Processed", orientation);
        telemetry.addData("Raw", reading);
        telemetry.addData("Bias", bias);
    }

}

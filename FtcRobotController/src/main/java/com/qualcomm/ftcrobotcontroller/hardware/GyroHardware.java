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
    private double lastReading, orientation;
    public static final IntegrationMode mode = IntegrationMode.LEFT;

    @Override
    public void init(OpMode mode) {
        hardwareMap = mode.hardwareMap;
        telemetry = mode.telemetry;
        gyro = hardwareMap.gyroSensor.get("gyro");
        lastReading = 0;
        orientation = 0;
    }

    public double readSensor() {
        return gyro.getRotation();
    }

    @Override
    public void loop(long msSinceLastLoop) {
        double reading = readSensor();
        switch (mode) {
            case LEFT:
                orientation += msSinceLastLoop * reading / 1000;
                break;
            case TRAPEZOIDAL:
                orientation += msSinceLastLoop * (reading + lastReading) / 2000;
                break;
        }
        log("Orientation: " + orientation);
        telemetry.addData("Orientation", orientation);
    }

}

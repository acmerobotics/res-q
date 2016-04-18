package com.acmerobotics.library.examples;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.List;

public class InternalSensorScanner extends OpMode {

    private SensorManager sensorManager;

    @Override
    public void init() {
        sensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void loop() {
        RobotLog.i("scanning for sensors");
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            RobotLog.i("sensor: " + sensor.toString());
            telemetry.addData(sensor.getName(), sensor.getVersion());
        }
    }
}

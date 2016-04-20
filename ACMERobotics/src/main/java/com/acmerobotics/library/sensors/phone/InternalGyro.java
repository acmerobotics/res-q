package com.acmerobotics.library.sensors.phone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.acmerobotics.library.sensors.GyroSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

public class InternalGyro implements GyroSensor, SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    private double yaw, pitch, roll;

    public InternalGyro(OpMode mode) {
        sensorManager = (SensorManager) mode.hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        yaw = 0;
        pitch = 0;
        roll = 0;
    }

    @Override
    public double getYaw() {
        return yaw;
    }

    @Override
    public double getPitch() {
        return pitch;
    }

    @Override
    public double getRoll() {
        return roll;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        RobotLog.i("event: " + event.timestamp);
        float[] values = event.values;
        roll = values[0];
        pitch = values[1];
        yaw = values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

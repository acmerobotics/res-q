package com.acmerobotics.library.sensors.phone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.acmerobotics.library.sensors.types.OrientationSensor;
import com.acmerobotics.library.vector.Vector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

public class InternalOrientation implements OrientationSensor, SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    private double yaw, pitch, roll;

    public InternalOrientation(OpMode mode) {
        sensorManager = (SensorManager) mode.hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        yaw = 0;
        pitch = 0;
        roll = 0;
    }

    @Override
    public double getOrientationYaw() {
        return yaw;
    }

    @Override
    public double getOrientationPitch() {
        return pitch;
    }

    @Override
    public double getOrientationRoll() {
        return roll;
    }

    @Override
    public Vector getOrientation() {
        return new Vector(roll, pitch, yaw);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        RobotLog.i("event: " + event.timestamp);
        float[] values = event.values;
        yaw = values[0];
        pitch = values[1];
        roll = values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

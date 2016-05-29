package com.acmerobotics.library.sensors.types;

import com.acmerobotics.library.vector.Vector;

public interface GyroSensor {

    /** get the yaw (rotation about z-axis) */
    public double getAngularVelocityYaw();

    /** get the pitch (rotation about the y-axis) */
    public double getAngularVelocityPitch();

    /** get the roll (rotation about the x-axis) */
    public double getAngularVelocityRoll();

    /** get it as a vector */
    public Vector getAngularVelocity();

}

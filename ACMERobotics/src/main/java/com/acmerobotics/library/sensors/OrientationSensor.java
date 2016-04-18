package com.acmerobotics.library.sensors;

public interface OrientationSensor {

    /** get the yaw (rotation about z-axis) */
    public double getYaw();

    /** get the pitch (rotation about the y-axis) */
    public double getPitch();

    /** get the roll (rotation about the x-axis) */
    public double getRoll();

}

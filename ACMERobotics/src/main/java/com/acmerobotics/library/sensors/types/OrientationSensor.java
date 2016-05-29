package com.acmerobotics.library.sensors.types;

import com.acmerobotics.library.vector.Vector;

public interface OrientationSensor {

    /** get the yaw (rotation about z-axis) */
    public double getOrientationYaw();

    /** get the pitch (rotation about the y-axis) */
    public double getOrientationPitch();

    /** get the roll (rotation about the x-axis) */
    public double getOrientationRoll();

    /** get the whole angular orientation vector */
    public Vector getOrientation();

}

package com.acmerobotics.library.vector;

public class Vector {

    public double x, y, z;
    public long time;

    public enum Axis {
        X, Y, Z
    }

    public Vector() {
        this(0, 0, 0);
    }

    public Vector(double x, double y, double z) {
        this(x, y, z, System.nanoTime());
    }

    public Vector(double x, double y, double z, long t) {
        this.x = x;
        this.y = y;
        this.z = z;
        time = t;
    }

    public String toString() {
        return String.format("(%5.1f,%5.1f,%5.1f)", x, y, z);
    }

}

package com.acmerobotics.library.vector;

public class Vector {

    public double x, y, z;
    public long time;

    private static final String NUMBER_FORMAT = "%+07.2f";
    private static final String VECTOR_FORMAT = "(" + NUMBER_FORMAT + "," + NUMBER_FORMAT + "," + NUMBER_FORMAT + ")";

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
        return String.format(VECTOR_FORMAT, x, y, z);
    }

}

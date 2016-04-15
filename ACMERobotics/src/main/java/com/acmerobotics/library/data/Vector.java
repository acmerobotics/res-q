package com.acmerobotics.library.data;

public class Vector {

    public double x, y, z;

    public enum Axis {
        X, Y, Z
    }

    public Vector() {
        this(0, 0, 0);
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}

package com.acmerobotics.library.graph;

import com.acmerobotics.library.data.Vector;

public class VectorComponentNode<T extends Vector> extends Node<T, Double> {

    private Vector.Axis axis;

    public VectorComponentNode(Vector.Axis axis) {
        this.axis = axis;
    }

    public Vector.Axis getAxis() {
        return axis;
    }

    @Override
    public Double process(T i) {
        switch(axis) {
            case X:
                return i.x;
            case Y:
                return i.y;
            case Z:
                return i.z;
            default:
                return null;
        }
    }
}

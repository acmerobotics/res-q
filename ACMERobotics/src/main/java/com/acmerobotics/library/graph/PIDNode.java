package com.acmerobotics.library.graph;

import com.acmerobotics.library.data.TimestampedData;

/**
 * Created by Admin on 2/4/2016.
 */
public class PIDNode<T extends Double> extends Node<TimestampedData<T>, Double> {

    private TimestampedData<T> lastData = null;
    private double sumError = 0;
    private double kP, kI, kD;

    public PIDNode(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    @Override
    public Double process(TimestampedData<T> data) {
        double error = data.getData().doubleValue() - lastData.getData().doubleValue(),
                dt = ((double) data.getTimeSince(lastData)) / Math.pow(10, 6),
                rate = error / dt;
        sumError += error * dt;
        return kP * error + kI * sumError + kD * rate;
    }
}

package com.qualcomm.ftcrobotcontroller.data;

/**
 * Created by Admin on 2/4/2016.
 */
public class PIDController<T extends Double> {

    private TimestampedData<T> lastData = null;
    private double sumError = 0, output = 0;
    private double kP, kI, kD;

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void add(TimestampedData<T> data) {
        if (lastData == null) {
            lastData = data;
            return;
        }
        double error = data.getData().doubleValue() - lastData.getData().doubleValue(),
               dt = ((double) data.getTimeSince(lastData)) / Math.pow(10, 9),
               rate = error / dt;
        sumError += error * dt;
        output = kP * error + kI * sumError + kD * rate;
        lastData = data;
    }

    public double getOutput() {
        return output;
    }
}

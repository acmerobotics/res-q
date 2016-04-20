package com.acmerobotics.library.stream;

import java.util.concurrent.TimeUnit;

/** Uses trapezoidal rule */
public class DataIntegrator extends DataNode<Double, Double> {
    
    private double sum;
    private Data<Double> lastVal;
    
    public DataIntegrator() {
        sum = 0;
        lastVal = Data.wrap(0D, 0);
    }

    @Override
    public Data<Double> process(Data<Double> d) {
        if (!d.isTimestamped()) return null;

        sum += ((d.get() + lastVal.get()) / 2.0) * TimeUnit.MICROSECONDS.toSeconds(d.getTime() - lastVal.getTime());
        lastVal = d;
        return Data.wrap(sum, d.getTime());
    }

}

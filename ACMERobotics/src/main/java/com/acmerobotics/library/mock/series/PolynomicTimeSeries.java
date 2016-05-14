package com.acmerobotics.library.mock.series;

public class PolynomicTimeSeries extends ContinuousTimeSeries {

    private Polynomial polynomial;

    public PolynomicTimeSeries(Polynomial poly) {
        polynomial = poly;
    }

    @Override
    public Datum get(Timestamp timestamp) {
        return new Datum(timestamp, polynomial.get(timestamp.inSeconds()));
    }
}

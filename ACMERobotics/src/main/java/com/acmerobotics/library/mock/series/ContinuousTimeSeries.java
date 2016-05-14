package com.acmerobotics.library.mock.series;

public abstract class ContinuousTimeSeries {

    public abstract Datum get(Timestamp timestamp);

    public DiscreteTimeSeries discretize(Timestamp incr) {
        return discretize(new Timestamp(0), incr);
    }

    public DiscreteTimeSeries discretize(final Timestamp start, final Timestamp incr) {
        return new DiscreteTimeSeries() {
            @Override
            public Datum get() {
                Datum datum = ContinuousTimeSeries.this.get(start);
                start.incr(incr);
                return datum;
            }
        };
    }

}

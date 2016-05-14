package com.acmerobotics.library.mock.series;

import java.util.ArrayList;

public class DiscreteTimeSeries {

    private ArrayList<Datum> data;

    public DiscreteTimeSeries() {
        this(new ArrayList<Datum>());
    }

    public DiscreteTimeSeries(ArrayList<Datum> data) {
        this.data = data;
    }

    public void add(Datum d) {
        data.add(d);
    }

    public void addWithTimestamp(double d) {
        add(new Datum(Timestamp.now(), d));
    }

    public Datum get() {
        return data.remove(0);
    }

    public boolean hasMoreData() {
        return data.size() == 0;
    }

}

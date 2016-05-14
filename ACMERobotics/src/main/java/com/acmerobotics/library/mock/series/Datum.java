package com.acmerobotics.library.mock.series;

public class Datum {

    private Timestamp timestamp;
    private double contents;

    public Datum(Timestamp time, double contents) {
        timestamp = time;
        this.contents = contents;
    }
}

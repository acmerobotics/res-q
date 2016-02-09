package com.acmerobotics.library.i2c;

public class TimestampedData<T> {

    public final T data;
    public final long nanoTime;

    public TimestampedData(T data) {
        this.data = data;
        this.nanoTime = System.nanoTime();
    }
}

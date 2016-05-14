package com.acmerobotics.library.mock.series;

public class Timestamp {

    long nanoTime;

    public Timestamp() {
        this(System.nanoTime());
    }

    public Timestamp(long time) {
        nanoTime = time;
    }

    public long inNanos() {
        return nanoTime;
    }

    public double inMillis() {
        return getDouble() / Math.pow(10, 6);
    }

    public double inSeconds() {
        return getDouble() / Math.pow(10, 9);
    }

    public long get() {
        return nanoTime;
    }

    private double getDouble() {
        return (double) nanoTime;
    }

    public Timestamp add(Timestamp time) {
        return new Timestamp(time.get() + get());
    }

    public void incr(Timestamp time) {
        nanoTime += time.get();
    }

    public static Timestamp now() {
        return new Timestamp(System.nanoTime());
    }

}

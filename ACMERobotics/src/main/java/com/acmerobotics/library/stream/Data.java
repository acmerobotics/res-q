package com.acmerobotics.library.stream;

public class Data<T> {

    private boolean timestamped;
    private long time;
    private T datum;

    private Data(T d) {
        datum = d;
        timestamped = false;
        time = 0;
    }

    private Data(T d, long t) {
        datum = d;
        timestamped = true;
        time = t;
    }

    public static <T> Data<T> wrap(T d) {
        return new Data(d);
    }

    public static <T> Data<T> wrap(T d, long t) {
        return new Data(d, t);
    }

    public static <T> Data<T> wrapTime(T d) {
        return new Data(d, System.nanoTime());
    }

    public T get() {
        return datum;
    }

    public boolean isTimestamped() {
        return timestamped;
    }

    public long getTime() {
        if (isTimestamped()) return time;
        else return 0L;
    }

}

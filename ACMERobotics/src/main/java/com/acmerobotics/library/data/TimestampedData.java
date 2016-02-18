package com.acmerobotics.library.data;

public class TimestampedData<T> {

    private long timestamp;
    private T data;

    private TimestampedData(T data) {
        this(data, System.nanoTime());
    }

    private TimestampedData(T data, long time) {
        this.data = data;
        this.timestamp = time;
    }

    public static <T> TimestampedData<T> wrap(T data) {
        return new TimestampedData<T>(data);
    }

    public static <T> TimestampedData<T> wrap(T data, long timestamp) {
        return new TimestampedData<T>(data, timestamp);
    }

    public T getData() {
        return this.data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTimeSince(TimestampedData timestampedData) {
        return getTimestamp() - timestampedData.getTimestamp();
    }
}
